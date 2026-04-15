package com.chirplingo.client.impls;

import com.chirplingo.client.base.BaseSupabaseClient;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.client.interfaces.RemoteStorageClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class SupabaseStorageClient extends BaseSupabaseClient implements RemoteStorageClient {
    public SupabaseStorageClient(SupabaseContext context) {
        super(context);
    }

    @Override
    public String upload(String bucket, String path, byte[] data) {
        try {
            String endpoint = "/storage/v1/object/" + bucket + "/" + path;

            HttpRequest request = createRequest(endpoint)
                .header("Content-Type", "application/octet-stream")
                .header("x-upsert", "true") 
                .POST(HttpRequest.BodyPublishers.ofByteArray(data))
                .build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            
            if (status == 201 || status == 200) {
                return getPublicUrl(bucket, path);
            } else {
                System.err.println("Lỗi upload Storage: " + response.body());
                return null;
            }
        } catch(Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String getPublicUrl(String bucket, String path) {
        return context.getUrl() + "/storage/v1/object/public/" + bucket + "/" + path;
    }

    @Override
    public boolean delete(String bucket, String path) {
        try {
            String endpoint = "/storage/v1/object/" + bucket + "/" + path;

            HttpRequest request = createRequest(endpoint)
                    .DELETE()
                    .build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());
            
            int status = response.statusCode();
            if (status == 200 || status == 204) {
                return true;
            } else {
                System.err.println("Lỗi xoá Storage [" + status + "]: " + response.body());
                return false;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}
