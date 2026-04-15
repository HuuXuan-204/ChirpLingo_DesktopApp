package com.chirplingo.utils;

import io.github.cdimascio.dotenv.Dotenv;

public class Config {
    private static Dotenv dotenv;

    public static void load() {
        try {
            dotenv = Dotenv.configure()
                    .ignoreIfMalformed()
                    .ignoreIfMissing()
                    .load();
        } catch (Exception e) {
            System.err.println("Cảnh báo: Không tìm thấy file .env");
            e.printStackTrace();
        }
    }

    public static String get(String key) {
        if (dotenv == null) {
            return System.getenv(key);
        }
        String value = dotenv.get(key);
        return value != null ? value : System.getenv(key);
    }
}
