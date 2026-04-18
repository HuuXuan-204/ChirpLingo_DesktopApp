package com.chirplingo.client.base;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import java.net.http.HttpClient;

public class SupabaseContext {
    private final String url;
    private final String anonKey;
    private String accessToken;
    private final HttpClient httpClient;
    private final ObjectMapper mapper;

    public SupabaseContext(String url, String anonKey, HttpClient httpClient, String accessToken) {
        this.url = url;
        this.anonKey = anonKey;
        this.httpClient = httpClient;
        this.accessToken = accessToken;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.setPropertyNamingStrategy(PropertyNamingStrategies.SNAKE_CASE);
    }

    public String getUrl() {
        return url;
    }

    public String getAnonKey() {
        return anonKey;
    }

    public HttpClient getHttpClient() {
        return httpClient;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public ObjectMapper getMapper() {
        return mapper;
    }
}
