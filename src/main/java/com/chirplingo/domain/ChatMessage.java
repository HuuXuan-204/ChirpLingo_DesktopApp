package com.chirplingo.domain;

import java.time.OffsetDateTime;

public class ChatMessage {
    private String role;
    private String content;
    private OffsetDateTime timestamp;
     public ChatMessage(String role, String content, OffsetDateTime timestamp ){
        this.role = role;
        this.content = content;
        this.timestamp = timestamp;
    }    
    public String getContent(){
        return content;
    }
    public boolean isUser(){
        return role.equalsIgnoreCase("user");
    }
    public String getRole(){
        return role;
    }
    public OffsetDateTime getTimestamp(){
        return timestamp;
    } 

}
