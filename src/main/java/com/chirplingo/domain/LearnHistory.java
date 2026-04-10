package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.util.LinkedList;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.temporal.ChronoUnit;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;

public class LearnHistory extends BaseEntity {
    private IntegerProperty monday;
    private IntegerProperty tuesday;
    private IntegerProperty wednesday;
    private IntegerProperty thursday;
    private IntegerProperty friday;
    private IntegerProperty saturday;
    private IntegerProperty sunday;
    private IntegerProperty streak;

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
                throw new IllegalStateException("Unexpected index: " + index);
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
                throw new IllegalStateException("Unexpected index: " + index);
        }
    }

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
                throw new IllegalStateException("Unexpected index: " + index);
        }
    }

    public int getStreak() {
        return this.streak.get();
    }

    public IntegerProperty streakProperty() {
        return this.streak;
    }

    public void updateToday(int addedValue) {
        OffsetDateTime now = OffsetDateTime.now(ZoneOffset.UTC);
        
        
        boolean needUpdate = refreshDayValues(now);
        
        if (addedValue > 0) refreshStreak(now);

        int todayIndex = now.getDayOfWeek().getValue() - 1;
        int currentValue = getDayValue(todayIndex);
        setDayValue(todayIndex, currentValue + addedValue);
        if (needUpdate || addedValue > 0)
        triggerUpdate();

    }

    private boolean refreshDayValues(OffsetDateTime now) {
        LinkedList<Integer> oldDayValue = new LinkedList<>();

        for (int i = 0; i < 7; i++) {
            oldDayValue.add(getDayValue(i));
        }

        OffsetDateTime nowDate = now;
        OffsetDateTime lastUpdateDate = this.updatedAt;

        if (!nowDate.toLocalDate().isEqual(lastUpdateDate.toLocalDate())) {
            // Tìm ngày thứ Hai của tuần hiện tại và thứ Hai của tuần trước
            LocalDate startOfCurrentWeek = nowDate.toLocalDate()
                    .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
            LocalDate startOfLastWeek = startOfCurrentWeek.minusWeeks(1);

            int todayIndex = nowDate.getDayOfWeek().getValue() - 1;
            for (int i = 0; i < 7; i++) {
                if (i == todayIndex) {
                    setDayValue(i, 0);
                } else if (i < todayIndex) {
                    // Các ngày trước hôm nay: Nếu lần cập nhật cuối là trước tuần này -> Reset về 0
                    if (lastUpdateDate.toLocalDate().isBefore(startOfCurrentWeek)) {
                        setDayValue(i, 0);
                    }
                } else {
                    // Nếu lần cập nhật cuối là trước tuần trước nữa (Nghỉ > 2 tuần) -> Reset về 0
                    if (lastUpdateDate.toLocalDate().isBefore(startOfLastWeek)) {
                        setDayValue(i, 0);
                    }
                }
            }
            for (int i = 0; i < 7; i++) {
                if (getDayValue(i) != oldDayValue.get(i)) {
                    return true;
                }
            }
        }
        return false;
    }

    private void refreshStreak(OffsetDateTime now) {
        if (this.updatedAt != null) {
            OffsetDateTime nowDate = now;
            OffsetDateTime lastUpdateDate = this.updatedAt;

            long daysBetween = ChronoUnit.DAYS.between(lastUpdateDate.toLocalDate(), nowDate.toLocalDate());

            if (daysBetween == 1) {
                this.streak.set(this.streak.get() + 1);
            } else if (daysBetween > 1) {
                this.streak.set(1);
            }
        } else {
            this.streak.set(0);
        }

    }
}