package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import com.chirplingo.practice.SRSResult;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;

public class Vocabulary extends BaseEntity {
    private StringProperty word;
    private StringProperty ipa;
    private StringProperty type;
    private StringProperty meaning;
    private StringProperty example;
    private StringProperty note;

    private String userId;
    private OffsetDateTime nextReviewAt;
    private int interval;
    private double easeFactor;
    private int repetition;
    private OffsetDateTime deletedAt;

    public Vocabulary(String id, OffsetDateTime createdAt, OffsetDateTime updatedAt, boolean isSynced,
            String word, String ipa, String type, String meaning,
            String example, String note, String userId, OffsetDateTime nextReviewAt, int interval,
            double easeFactor, int repetition, OffsetDateTime deletedAt) {
        super(id, createdAt, updatedAt, isSynced);
        this.word = new SimpleStringProperty(word);
        this.ipa = new SimpleStringProperty(ipa);
        this.type = new SimpleStringProperty(type);
        this.meaning = new SimpleStringProperty(meaning);
        this.example = new SimpleStringProperty(example);
        this.note = new SimpleStringProperty(note);
        this.userId = userId;
        this.nextReviewAt = nextReviewAt;
        this.interval = interval;
        this.easeFactor = easeFactor;
        this.repetition = repetition;
        this.deletedAt = deletedAt;
    }

    public String getWord() {
        return word.get();
    }

    public void setWord(String word) {
        this.word.set(word);
        triggerUpdate();
    }

    public StringProperty wordProperty() {
        return word;
    }

    public String getIpa() {
        return ipa.get();
    }

    public void setIpa(String ipa) {
        this.ipa.set(ipa);
        triggerUpdate();
    }

    public StringProperty ipaProperty() {
        return ipa;
    }

    public String getType() {
        return type.get();
    }

    public void setType(String type) {
        this.type.set(type);
        triggerUpdate();
    }

    public StringProperty typeProperty() {
        return type;
    }

    public String getMeaning() {
        return meaning.get();
    }

    public void setMeaning(String meaning) {
        this.meaning.set(meaning);
        triggerUpdate();
    }

    public StringProperty meaningProperty() {
        return meaning;
    }

    public String getExample() {
        return example.get();
    }

    public void setExample(String example) {
        this.example.set(example);
        triggerUpdate();
    }

    public StringProperty exampleProperty() {
        return example;
    }

    public String getNote() {
        return note.get();
    }

    public void setNote(String note) {
        this.note.set(note);
        triggerUpdate();
    }

    public StringProperty noteProperty() {
        return note;
    }

    public String getUserId() {
        return userId;
    }

    public OffsetDateTime getNextReviewAt() {
        return nextReviewAt;
    }

    public int getInterval() {
        return interval;
    }

    public double getEaseFactor() {
        return easeFactor;
    }

    public int getRepetition() {
        return repetition;
    }

    public OffsetDateTime getDeletedAt() {
        return deletedAt;
    }

    public void setSRSInfo(SRSResult result) {
        this.nextReviewAt = result.getNextReviewAt();
        this.interval = result.getInterval();
        this.easeFactor = result.getEaseFactor();
        this.repetition = result.getRepetition();
        triggerUpdate();
    }

    public boolean isDue() {
        if (this.nextReviewAt == null) {
            return true; 
        }
        return nextReviewAt.isBefore(OffsetDateTime.now(ZoneOffset.UTC));
    }

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now(ZoneOffset.UTC);
        triggerUpdate();
    }
}
