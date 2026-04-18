package com.chirplingo.client.impls;

import com.chirplingo.client.base.BaseSupabaseClient;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.client.interfaces.RemoteTableClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;

public class SupabaseTableClient<T> extends BaseSupabaseClient implements RemoteTableClient<T> {
    private static final int PAGE_SIZE = 500;
    private String tableName;
    private Class<T> entityClass;

    public SupabaseTableClient(SupabaseContext context, String tableName, Class<T> entityClass) {
        super(context);
        this.tableName = tableName;
        this.entityClass = entityClass;
    }

    @Override
    public boolean pushUnsynced(List<T> items) {
        if (items == null || items.isEmpty()) {
            return true;
        }
        try {
            for (int i = 0; i < items.size(); i += PAGE_SIZE) {
                int end = Math.min(i + PAGE_SIZE, items.size());
                List<T> batch = items.subList(i, end);

                String payload = context.getMapper().writeValueAsString(batch);
                HttpRequest request = createRequest("/rest/v1/" + tableName)
                        .header("Prefer", "resolution=merge-duplicates")
                        .POST(HttpRequest.BodyPublishers.ofString(payload))
                        .build();

                HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();
                if (status == 201 || status == 200) {
                    System.out.println("[Push][" + tableName + "] Đã đẩy batch " + (i / PAGE_SIZE + 1)
                            + ": " + batch.size() + " items.");
                } else {
                    System.err.println("Lỗi Push [" + status + "]: " + response.body());
                    return false;
                }
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<T> pullChanges(OffsetDateTime lastSyncTime) {
        List<T> allItems = new ArrayList<>();
        int page = 0;

        String query = "/rest/v1/" + tableName
                + "?updated_at=gt." + lastSyncTime.toInstant().toString()
                + "&order=updated_at.asc";

        try {
            while (true) {
                int start = page * PAGE_SIZE;
                int end   = start + PAGE_SIZE - 1;

                HttpRequest request = createRequest(query)
                        .header("Range", start + "-" + end)
                        .GET()
                        .build();

                HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
                int status = response.statusCode();

                if (status == 200 || status == 206) {
                    List<T> pageItems = context.getMapper().readValue(
                        response.body(),
                        context.getMapper().getTypeFactory().constructCollectionType(List.class, entityClass)
                    );

                    if (pageItems == null || pageItems.isEmpty()) break;

                    allItems.addAll(pageItems);
                    System.out.println("[Pull][" + tableName + "] Trang " + page
                            + ": nhận " + pageItems.size() + " items (tổng: " + allItems.size() + ")");

                    if (pageItems.size() < PAGE_SIZE) break;

                    page++;
                } else {
                    System.err.println("[Pull][" + tableName + "] Lỗi HTTP trang " + page
                            + " [" + status + "]: " + response.body());
                    return null;
                }
            }
        } catch (Exception e) {
            System.err.println("[Pull][" + tableName + "] Exception: " + e.getMessage());
            e.printStackTrace();
            return null;
        }

        System.out.println("[Pull][" + tableName + "] Hoàn tất: tổng " + allItems.size() + " items từ " + page + " trang.");
        return allItems;
    }
}
