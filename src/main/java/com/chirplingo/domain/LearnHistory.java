package com.chirplingo.domain;

import com.chirplingo.domain.base.BaseEntity;
import java.time.DayOfWeek;
import java.time.LocalDate;

public class LearnHistory extends BaseEntity {
    private int monday;
    private int tuesday;
    private int wednesday;
    private int thursday;
    private int friday;
    private int saturday;
    private int sunday;
    private int streak;

    public LearnHistory(int monday, int tuesday, int wednesday, int thursday, int friday, int saturday, int sunday,
            int streak) {
        this.monday = monday;
        this.tuesday = tuesday;
        this.wednesday = wednesday;
        this.thursday = thursday;
        this.friday = friday;
        this.saturday = saturday;
        this.sunday = sunday;
        this.streak = streak;
    }

    public int getDayValue() {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        switch (today) {
            case MONDAY:
                return monday;
            case TUESDAY:
                return tuesday;
            case WEDNESDAY:
                return wednesday;
            case THURSDAY:
                return thursday;
            case FRIDAY:
                return friday;
            case SATURDAY:
                return saturday;
            case SUNDAY:
                return sunday;
        }

        throw new IllegalStateException("Unexpected DayOfWeek: " + today);

    }

    public void setDayValue(int value) {
        DayOfWeek today = LocalDate.now().getDayOfWeek();

        switch (today) {
            case MONDAY:
                monday = value;
                break;
            case TUESDAY:
                tuesday = value;
                break;
            case WEDNESDAY:
                wednesday = value;
                break;
            case THURSDAY:
                thursday = value;
                break;
            case FRIDAY:
                friday = value;
                break;
            case SATURDAY:
                saturday = value;
                break;
            case SUNDAY:
                sunday = value;
                break;
        }
    }

    public int getStreak() {
        return streak;
    }

    public void setStreak(int streak) {
        this.streak = streak;
    }

    public void increaseToday() {
        setDayValue(getDayValue() + 1);
    }

    public void refreshStreak() {
        if (getDayValue() > 0) {
            streak++;
        } else {
            streak = 0;
        }
    }

    public boolean refreshDayValue() {
        if (getDayValue() > 0) {
            setDayValue(0);
            return true;
        }
        return false;
    }

}
