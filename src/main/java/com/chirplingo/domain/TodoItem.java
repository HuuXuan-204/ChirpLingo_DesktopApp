package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

import java.time.OffsetDateTime;
import com.chirplingo.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonProperty;

public class TodoItem extends BaseEntity {
    private StringProperty content;
    private BooleanProperty isFinished;
    private OffsetDateTime deadline;
    private String userId;
    private OffsetDateTime deletedAt;

    public TodoItem() {
        super();
        this.content = new SimpleStringProperty();
        this.isFinished = new SimpleBooleanProperty();
    }

    public TodoItem(String id, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean isSynced,
            String content, boolean isFinished, OffsetDateTime deadline, String userId,
            OffsetDateTime deletedAt) {
        super(id, createdAt, updatedAt, isSynced);
        this.content = new SimpleStringProperty(content);
        this.isFinished = new SimpleBooleanProperty(isFinished);
        this.deadline = deadline;
        this.userId = userId;
        this.deletedAt = deletedAt;
    }

    public String getContent() {
        return content.get();
    }

    public void setContent(String content) {
        this.content.set(content);
        triggerUpdate();
    }

    public StringProperty contentProperty() {
        return content;
    }

    @JsonProperty("is_finished")
    public boolean isFinished() {
        return isFinished.get();
    }

    @JsonProperty("is_finished")
    public void setFinished(boolean finished) {
        this.isFinished.set(finished);
    }

    public BooleanProperty isFinishedProperty() {
        return isFinished;
    }

    public OffsetDateTime getDeadline() {
        return deadline;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setDeletedAt(OffsetDateTime deletedAt) {
        this.deletedAt = deletedAt;
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }

    public void toggleStatus() {
        this.isFinished.set(!this.isFinished.get());
        triggerUpdate();
    }

    public void setDeadline(OffsetDateTime deadline) {
        this.deadline = deadline;
        triggerUpdate();
    }

    public boolean isOverdue() {
        if (this.deadline == null) {
            return false;
        }
        return this.deadline.isBefore(CommonUtils.getOffsetDateTime());
    }

    public void softDelete() {
        this.deletedAt = CommonUtils.getOffsetDateTime();
        triggerUpdate();
    }
}