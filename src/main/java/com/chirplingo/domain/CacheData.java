package com.chirplingo.domain;

import java.time.LocalDate;

public class CacheData {
    private String key;
    private String value;
    private LocalDate updatedAt;

    public CacheData(String key, String value, LocalDate updatedAt){
        this.key = key;
        this.value = value;
        this.updatedAt = updatedAt;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value;
    }

    public LocalDate getUpdatedAt(){
        return updatedAt;
    }

    public void setValue(String value){
        this.value = value;
    }
    
    public void setUpdatedAt( LocalDate updatedDate){
        this.updatedAt = updatedDate;
    }
}

