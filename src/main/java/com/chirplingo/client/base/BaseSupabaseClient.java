package com.chirplingo.client.base;

import java.net.URI;
import java.net.http.HttpRequest;
import java.time.Duration;

public abstract class BaseSupabaseClient {
    protected SupabaseContext context;

    public BaseSupabaseClient(SupabaseContext context) {
        this.context = context;
    }

    protected HttpRequest.Builder createRequest(String endpoint) {
        String url = context.getUrl() + endpoint;
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", context.getKey())
                .header("Authorization", "Bearer " + context.getKey())
                .header("Content-Type", "application/json");
    }
}
