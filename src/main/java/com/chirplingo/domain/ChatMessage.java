package com.chirplingo.domain;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ChatMessage {
    @JsonProperty
    private String role;
    
    @JsonProperty
    private String content;
    
    @JsonProperty
    private OffsetDateTime timestamp;

    public ChatMessage() {
    }

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
