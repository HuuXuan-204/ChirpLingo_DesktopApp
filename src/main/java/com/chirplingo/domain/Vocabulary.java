package com.chirplingo.domain;

import java.time.OffsetDateTime;

import com.chirplingo.domain.base.BaseEntity;

public class Vocabulary extends BaseEntity {
    private String word;
    private String ipa;
    private String type;
    private String meaning;
    private String example;
    private String note;
    private String userId;
    private OffsetDateTime nextReviewAt;
    private int interval;
    private double easeFactor;
    private int repetition;
    private OffsetDateTime deletedAt;

    public Vocabulary(String word, String ipa, String type, String meaning, String example, String note, String userId,
            OffsetDateTime nextReviewAt, int interval, double easeFactor, int repetition, OffsetDateTime deletedAt) {
        this.word = word;
        this.ipa = ipa;
        this.type = type;
        this.meaning = meaning;
        this.example = example;
        this.note = note;
        this.userId = userId;
        this.nextReviewAt = nextReviewAt;
        this.interval = interval;
        this.easeFactor = easeFactor;
        this.repetition = repetition;
        this.deletedAt = deletedAt;
    }

    public String getWord() {
        return word;
    }

    public String getIpa() {
        return ipa;
    }

    public String getType() {
        return type;
    }

    public String getMeaning() {
        return meaning;
    }

    public String getExample() {
        return example;
    }

    public String getNote() {
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

    public OffsetDateTime getDeleteAt() {
        return deletedAt;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public void setIpa(String ipa) {
        this.ipa = ipa;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    public void setExample(String example) {
        this.example = example;
    }

    public void setNote(String note) {
        this.note = note;
    }

    // quality = 0-5, nếu quality < 3 thì reset, >= 3 thì tăng repetition và tính interval mới
    public void updateReviewResult(int quality) {
        if (quality < 3) {
            this.repetition = 0;
            this.interval = 1;
        } else {
            this.repetition++;

            if (this.repetition == 1) {
                this.interval = 1;
            } else if (this.repetition == 2) {
                this.interval = 6;
            } else {
                this.interval = (int) Math.round(this.interval * this.easeFactor);
            }

            // Update ease factor theo thuật toán SM-2
            this.easeFactor = Math.max(1.3,
                    this.easeFactor + (0.1 - (5 - quality) * (0.08 + (5 - quality) * 0.02)));
        }

        this.nextReviewAt = OffsetDateTime.now().plusDays(this.interval);
    }

    public boolean isDue() {
        return this.nextReviewAt == null || !this.nextReviewAt.isAfter(OffsetDateTime.now());
    };

    public void softDelete() {
        this.deletedAt = OffsetDateTime.now();
    }

    public boolean isDeleted() {
        return this.deletedAt != null;
    }
}
