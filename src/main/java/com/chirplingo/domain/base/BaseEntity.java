package com.chirplingo.domain.base;

import java.time.OffsetDateTime;
import com.chirplingo.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;

import com.fasterxml.jackson.annotation.JsonProperty;

public abstract class BaseEntity implements Syncable{
    @JsonProperty("id")
    protected String id;
    
    @JsonProperty("created_at")
    protected OffsetDateTime createdAt;
    
    @JsonProperty("updated_at")
    protected OffsetDateTime updatedAt;
    
    @JsonIgnore
    protected boolean isSynced;

    public BaseEntity() {
        
    }
    public BaseEntity(String id, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean isSynced) {
        this.id = id;
        this.createdAt = createdAt;
        this.updatedAt = updatedAt;
        this.isSynced = isSynced;
    }


    @Override
    public String getId() {
        return this.id;
    };

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }



    @Override
    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }


    protected void triggerUpdate() {
        this.updatedAt = CommonUtils.getOffsetDateTime();

    }
    
    @Override
    public boolean isSynced() {
        return this.isSynced;
    }

    @Override
    public void markAsSynced() {
        this.isSynced = true;
    }

    @Override
    public void markAsUnsynced() {
        this.isSynced = false;
    }
}
