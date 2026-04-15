package com.chirplingo.utils;

import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.UUID;
import java.util.List;
import java.util.Collections;

public class CommonUtils {
    private CommonUtils() {
    }

    public static String generateUUID() {
        return UUID.randomUUID().toString();
    }

    public static OffsetDateTime getOffsetDateTime() {
        return OffsetDateTime.now(ZoneOffset.UTC);
    }

    public static OffsetDateTime parseDateTime(String value) {
        if (value == null || value.isBlank()) return null;
        try {
            return OffsetDateTime.parse(value);
        } catch (DateTimeParseException e) {
            try {
                return OffsetDateTime.parse(value.replace(" ", "T"));
            } catch (DateTimeParseException e2) {
                System.err.println("Không thể parse thời gian: [" + value + "]");
                return null;
            }
        }
    }

    public static LocalDate getLocalDate() {
        return LocalDate.now();
    }

    /**
     * Tráo thứ tự 1 List, dùng để tráo List Vocabulary trước khi ôn
     * @param <T> Kiểu dữ liệu của List cần tráo
     */
    public static <T> void suffle(List<T> list) {
        if(list != null && !list.isEmpty()) {
            Collections.shuffle(list);
        }
    }
}
