package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import java.time.OffsetDateTime;

public class TodoItem extends BaseEntity {
    private String content;
    private boolean isFinished;
    private OffsetDateTime deadline;
    private String userId;
    private OffsetDateTime deleteAt;

    public TodoItem(String content, boolean isFinished, OffsetDateTime deadline, String userId) {
        this.content = content;
        this.isFinished = isFinished;
        this.deadline = deadline;
        this.userId = userId;
    }

    public String getContent() {
        return content;
    }

    public OffsetDateTime getDeadline() {
        return deadline;
    }

    public String getUserId() {
        return userId;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setDeadline (OffsetDateTime deadline){
        this.deadline = deadline;
    }

    public void toggleStatus() {
        this.isFinished = !this.isFinished;
    }

    public boolean isOverdue() {
        return !isFinished && deadline.isBefore(OffsetDateTime.now());
    }

    public void softDelete(){
        this.deleteAt = OffsetDateTime.now();
    }

    public boolean isDeleted() {
        return this.deleteAt != null;
    }
}
