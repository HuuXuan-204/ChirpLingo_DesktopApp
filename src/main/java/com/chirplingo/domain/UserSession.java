package com.chirplingo.domain;

public class UserSession {
    private static UserSession instance;
    private String userId;

    private UserSession() {
    }

    public static synchronized UserSession getInstance() {
        if (instance == null){
            instance = new UserSession();
        }
        return instance;
    }

    public String getUserId(){
        return userId;
    }

    public void setUserId(String userId){
        this.userId = userId;
    }

    public boolean isActive() {
        return this.userId != null;
    }

    public void clear() {
        this.userId = null;
    }
}
