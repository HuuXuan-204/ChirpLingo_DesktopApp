package com.chirplingo.domain.base;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public abstract class BaseEntity implements Syncable{
    protected String id;
    protected OffsetDateTime createdAt;
    protected OffsetDateTime updatedAt;
    protected boolean isSynced;


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
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
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
