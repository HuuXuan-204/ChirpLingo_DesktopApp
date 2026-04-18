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
        String token = context.getAccessToken() != null ? context.getAccessToken() : context.getAnonKey();
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(15))
                .header("apikey", context.getAnonKey())
                .header("Authorization", "Bearer " + token)
                .header("Content-Type", "application/json");
    }
}
