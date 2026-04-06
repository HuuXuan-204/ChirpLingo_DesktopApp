package com.chirplingo.domain.base;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.LocalDateTime;

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

<<<<<<< HEAD
    protected void setCreatedAt(LocalDateTime time) {
        this.createdAt = time.atOffset(ZoneOffset.UTC);
    }

=======
>>>>>>> main
    @Override
    public OffsetDateTime getUpdatedAt() {
        return this.updatedAt;
    }

<<<<<<< HEAD
    protected void setUpdatedAt(LocalDateTime time) {
        this.updatedAt = time.atOffset(ZoneOffset.UTC);
=======
    protected void triggerUpdate() {
        this.updatedAt = OffsetDateTime.now(ZoneOffset.UTC);
>>>>>>> main
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
