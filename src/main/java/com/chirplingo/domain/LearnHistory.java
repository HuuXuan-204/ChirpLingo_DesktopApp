package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.temporal.TemporalAdjusters;
import com.chirplingo.utils.CommonUtils;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;

@JsonIgnoreProperties({"created_at", "createdAt"})
public class LearnHistory extends BaseEntity {
    private IntegerProperty monday;
    private IntegerProperty tuesday;
    private IntegerProperty wednesday;
    private IntegerProperty thursday;
    private IntegerProperty friday;
    private IntegerProperty saturday;
    private IntegerProperty sunday;
    private IntegerProperty streak;

    public LearnHistory() {
        super();
        this.monday = new SimpleIntegerProperty();
        this.tuesday = new SimpleIntegerProperty();
        this.wednesday = new SimpleIntegerProperty();
        this.thursday = new SimpleIntegerProperty();
        this.friday = new SimpleIntegerProperty();
        this.saturday = new SimpleIntegerProperty();
        this.sunday = new SimpleIntegerProperty();
        this.streak = new SimpleIntegerProperty();
    }

    public LearnHistory(String id, OffsetDateTime createdAt, OffsetDateTime updatedAt,
            boolean isSynced, int monday, int tuesday, int wednesday,
            int thursday, int friday, int saturday, int sunday, int streak) {
        super(id, createdAt, updatedAt, isSynced);
        this.monday = new SimpleIntegerProperty(monday);
        this.tuesday = new SimpleIntegerProperty(tuesday);
        this.wednesday = new SimpleIntegerProperty(wednesday);
        this.thursday = new SimpleIntegerProperty(thursday);
        this.friday = new SimpleIntegerProperty(friday);
        this.saturday = new SimpleIntegerProperty(saturday);
        this.sunday = new SimpleIntegerProperty(sunday);
        this.streak = new SimpleIntegerProperty(streak);
    }

    @JsonProperty("user_id")
    public String getUserId() {
        return this.getId();
    }

    @JsonProperty("user_id")
    public void setUserId(String userId) {
        this.id = userId;
    }

    @Override
    @JsonIgnore
    public OffsetDateTime getCreatedAt() {
        return super.getCreatedAt();
    }

    public int getMonday() { return monday.get(); }
    public void setMonday(int value) { this.monday.set(value); }

    public int getTuesday() { return tuesday.get(); }
    public void setTuesday(int value) { this.tuesday.set(value); }

    public int getWednesday() { return wednesday.get(); }
    public void setWednesday(int value) { this.wednesday.set(value); }

    public int getThursday() { return thursday.get(); }
    public void setThursday(int value) { this.thursday.set(value); }

    public int getFriday() { return friday.get(); }
    public void setFriday(int value) { this.friday.set(value); }

    public int getSaturday() { return saturday.get(); }
    public void setSaturday(int value) { this.saturday.set(value); }

    public int getSunday() { return sunday.get(); }
    public void setSunday(int value) { this.sunday.set(value); }

    public void setStreak(int value) { this.streak.set(value); }

    /**
     * Lấy giá trị số từ vựng đã học của ngày tương ứng
     * @param index Index của ngày (Thứ 2: 0, thứ 3: 1, thứ 4: 2, thứ 5: 3, thứ 6: 4, thứ 7: 5, Chủ nhật: 6)
     * @return int
     */
    public int getDayValue(int index) {
        switch (index) {
            case 0:
                return this.monday.get();
            case 1:
                return this.tuesday.get();
            case 2:
                return this.wednesday.get();
            case 3:
                return this.thursday.get();
            case 4:
                return this.friday.get();
            case 5:
                return this.saturday.get();
            case 6:
                return this.sunday.get();
            default:
                throw new IllegalStateException("Index không hợp lệ: " + index);
        }
    }

    public IntegerProperty getDayProperty(int index) {
        switch (index) {
            case 0:
                return this.monday;
            case 1:
                return this.tuesday;
            case 2:
                return this.wednesday;
            case 3:
                return this.thursday;
            case 4:
                return this.friday;
            case 5:
                return this.saturday;
            case 6:
                return this.sunday;
            default:
                throw new IllegalStateException("Index không hợp lệ: " + index);
        }
    }

    /**
     * Ghi đè giá trị số từ vựng đã học của ngày tương ứng
     * @param index Index của ngày (Thứ 2: 0, thứ 3: 1, thứ 4: 2, thứ 5: 3, thứ 6: 4, thứ 7: 5, Chủ nhật: 6)
     * @param value Số từ vựng đã học
     */
    private void setDayValue(int index, int value) {
        switch (index) {
            case 0:
                this.monday.set(value);
                break;
            case 1:
                this.tuesday.set(value);
                break;
            case 2:
                this.wednesday.set(value);
                break;
            case 3:
                this.thursday.set(value);
                break;
            case 4:
                this.friday.set(value);
                break;
            case 5:
                this.saturday.set(value);
                break;
            case 6:
                this.sunday.set(value);
                break;
            default:
                throw new IllegalStateException("Index không hợp lệ: " + index);
        }
    }

    /**
     * Lấy chuỗi ngày liên tiếp đã học từ vựng
     * @return int
     */
    public int getStreak() {
        return this.streak.get();
    }

    public IntegerProperty streakProperty() {
        return this.streak;
    }

    /**
     * Cập nhật số từ vựng và streak
     * @param addedValue Số từ vựng đã học
     */
    public void updateToday(int addedValue) {
        if (this.updatedAt == null) {
            if (addedValue > 0) {
                int todayIndex = CommonUtils.getOffsetDateTime().getDayOfWeek().getValue() - 1;
                setDayValue(todayIndex, addedValue);
                this.streak.set(1);
                triggerUpdate();
            }
            return;
        }

        OffsetDateTime now = CommonUtils.getOffsetDateTime();
        LocalDate nowDate = now.toLocalDate();
        LocalDate lastUpdate = this.updatedAt.toLocalDate();
        int todayIndex = now.getDayOfWeek().getValue() - 1;

        boolean hasUpdate = false;

        // Reset dayValue cũ
        if (!nowDate.isEqual(lastUpdate)) {
            LocalDate thisWeek = nowDate.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate lastWeek = thisWeek.minusWeeks(1);

            for (int i = 0; i < 7; i++) {
                boolean shouldReset = false;
                if (i == todayIndex) {
                    shouldReset = true; 
                } else if (i < todayIndex) {
                    shouldReset = lastUpdate.isBefore(thisWeek);
                } else {
                    shouldReset = lastUpdate.isBefore(lastWeek);
                }

                if (shouldReset) {
                    setDayValue(i, 0);
                    hasUpdate = true;
                }
            }
        }

        // Tính Streak
        boolean isFirstLessonToday = (getDayValue(todayIndex) == 0 && addedValue > 0);
        if (isFirstLessonToday) {
            int yesterdayIndex = (todayIndex + 6) % 7;

            if (getDayValue(yesterdayIndex) > 0) {
                this.streak.set(this.streak.get() + 1);
            } else {
                this.streak.set(1);
            }
            hasUpdate = true;
        }

        if (addedValue > 0) {
            setDayValue(todayIndex, getDayValue(todayIndex) + addedValue);
            hasUpdate = true;
        }

        if (hasUpdate) {
            triggerUpdate();
        }
    }

}