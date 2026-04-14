package com.chirplingo.practice.entities;

import java.time.OffsetDateTime;

/**
 * Kết quả của thuật toán SRS
 */
public class SRSResult {
    private OffsetDateTime nextReviewAt;
    private int interval;
    private double easeFactor;
    private int repetition;

    public SRSResult(OffsetDateTime nextReviewAt, int interval, double easeFactor, int repetition) {
        this.nextReviewAt = nextReviewAt;
        this.interval = interval;
        this.easeFactor = easeFactor;
        this.repetition = repetition;
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
}
