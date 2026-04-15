package com.chirplingo.client.impls;

import com.chirplingo.client.base.BaseSupabaseClient;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.client.interfaces.RemoteTableClient;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.Collections;

public class SupabaseTableClient<T> extends BaseSupabaseClient implements RemoteTableClient<T> {
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
            String payload = context.getMapper().writeValueAsString(items);
            HttpRequest request = createRequest("/rest/v1/" + tableName)
                    .header("Prefer", "resolution=merge-duplicates")
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            int status = response.statusCode();
            if (status == 201 || status == 200) {
                return true;
            } else {
                System.err.println("Lỗi Push [" + status + "]: " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    @Override
    public List<T> pullChanges(OffsetDateTime lastSyncTime) {
        try {
            String query = "?updated_at=gt." + lastSyncTime.toInstant().toString();
            HttpRequest request = createRequest("/rest/v1/" + tableName + query)
                    .GET()
                    .build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            int status = response.statusCode();
            if (status == 200 || status == 201) {
                return context.getMapper().readValue(
                    response.body(), 
                    context.getMapper().getTypeFactory().constructCollectionType(List.class, entityClass)
                );
            } else {
                System.err.println("Lỗi Pull [" + response.statusCode() + "]: " + response.body());
                return Collections.emptyList();
            }
        } catch (Exception e) {
            e.printStackTrace();
            return Collections.emptyList();
        }
    }
}
