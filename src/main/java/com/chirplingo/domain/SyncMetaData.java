package com.chirplingo.domain;

import java.time.OffsetDateTime;

public class SyncMetaData {
    private String userId;
    private String tableName;
    private OffsetDateTime lastSyncTime;

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
