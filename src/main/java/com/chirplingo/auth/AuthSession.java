package com.chirplingo.auth;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties(ignoreUnknown = true)
public class AuthSession {

    @JsonProperty("access_token")
    private String accessToken;

    @JsonProperty("refresh_token")
    private String refreshToken;

    @JsonProperty("expires_in")
    private long expiresIn;

    @JsonProperty("expires_at")
    private long expiresAt;

    @JsonProperty("token_type")
    private String tokenType = "bearer";

    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("email")
    private String email;

    public AuthSession() {
        
    }

    public AuthSession(String accessToken, String refreshToken, long expiresIn, String userId, String email) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiresIn = expiresIn;
        this.expiresAt = (System.currentTimeMillis() / 1000) + expiresIn;
        this.userId = userId;
        this.email = email;
    }

    public boolean isExpired() {
        return (System.currentTimeMillis() / 1000) >= (expiresAt - 60);
    }

    public boolean isValid() {
        return accessToken != null && refreshToken != null && userId != null;
    }

    public String getAccessToken() { return accessToken; }
    public String getRefreshToken() { return refreshToken; }
    public long   getExpiresIn() { return expiresIn; }
    public long   getExpiresAt() { return expiresAt; }
    public String getTokenType() { return tokenType; }
    public String getUserId() { return userId; }
    public String getEmail() { return email; }

    public void setAccessToken(String accessToken) { this.accessToken = accessToken; }
    public void setRefreshToken(String refreshToken) { this.refreshToken = refreshToken; }
    public void setExpiresIn(long expiresIn) { this.expiresIn = expiresIn; }
    public void setExpiresAt(long expiresAt) { this.expiresAt = expiresAt; }
    public void setTokenType(String tokenType) { this.tokenType = tokenType; }
    public void setUserId(String userId) { this.userId = userId; }
    public void setEmail(String email) { this.email = email; }
}
