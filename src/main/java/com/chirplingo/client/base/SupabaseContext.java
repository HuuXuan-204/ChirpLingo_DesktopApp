package com.chirplingo.client.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.http.HttpClient;

public class SupabaseContext {
    private final String url;
    private final String key;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public SupabaseContext(String url, String key, HttpClient httpClient) {
        this.url = url;
        this.key = key;
        this.httpClient = httpClient;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public String getUrl() {
        return url;
    }

    public String getKey() {
        return key;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
