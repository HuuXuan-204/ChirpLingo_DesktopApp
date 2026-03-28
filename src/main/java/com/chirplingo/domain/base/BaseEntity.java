package com.chirplingo.domain.base;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.LocalDateTime;

public abstract class BaseEntity implements Syncable{
    protected String id;
    protected OffsetDateTime createdAt;
    protected OffsetDateTime updatedAt;
    protected boolean isSynced;

    @Override
    public String getId() {
        return this.id;
    };

    protected void setId(String id) {
        this.id = id;
    }

    public OffsetDateTime getCreatedAt() {
        return this.createdAt;
    }

    protected void setCreatedAt(LocalDateTime time) {
        this.createdAt = time.atOffset(ZoneOffset.UTC);
    }

    @Override
    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

    protected void setUpdatedAt(LocalDateTime time) {
        this.updatedAt = time.atOffset(ZoneOffset.UTC);
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

    protected void triggerUpdate() {
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
    }
}
