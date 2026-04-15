package com.chirplingo.domain;

import java.time.OffsetDateTime;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SyncMetaData {
    @JsonProperty("user_id")
    private String userId;

    @JsonProperty("table_name")
    private String tableName;
    
    @JsonProperty("last_sync_time")
    private OffsetDateTime lastSyncTime;

    public SyncMetaData() {
    }

    public SyncMetaData(String userId, String tableName, OffsetDateTime lastSyncTime){
        this.userId = userId;
        this.tableName = tableName;
        this.lastSyncTime = lastSyncTime;
    }

    public String getUserId() {
        return userId;
    }

    public String getTableName(){
        return this.tableName;
    }

    public OffsetDateTime getLastSyncTime(){
        return this.lastSyncTime;
    }

    public void setLastSyncTime(OffsetDateTime lastSyncTime){
        this.lastSyncTime = lastSyncTime;
    } 
}
