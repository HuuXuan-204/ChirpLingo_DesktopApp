package com.chirplingo.repository.db;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;

public class DatabaseManager {
    private static volatile DatabaseManager instance;
    private static final String dbURL = "jdbc:sqlite:chirplingo.db";
    private ExecutorService executor;

    private DatabaseManager() {
        this.executor = Executors.newSingleThreadExecutor();
        initializeDatabase();
    }

    public static DatabaseManager getInstance() {
        if (instance == null) {
            synchronized (DatabaseManager.class) {
                if (instance == null) {
                    instance = new DatabaseManager();
                }
            }
        }
        
        return instance;
    }

    public Connection getConnection() {
        try {
            Connection conn = DriverManager.getConnection(dbURL);
            return conn;
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void initializeDatabase() {
        executor.execute(() -> {
           try (Connection conn = getConnection(); Statement st = conn.createStatement()) {
                String[] sqlCommands = {
                    "PRAGMA journal_mode = WAL;",
                    "PRAGMA busy_timeout = 10000;",
                    "PRAGMA synchronous = NORMAL;",
                    "PRAGMA cache_size = -16000;",
                    
                    "CREATE TABLE IF NOT EXISTS profile (" +
                    "    id TEXT PRIMARY KEY NOT NULL," +
                    "    email TEXT," +
                    "    user_name TEXT," +
                    "    avatar TEXT," +
                    "    created_at TEXT," +
                    "    updated_at TEXT," +
                    "    is_synced INTEGER DEFAULT 0" +
                    ");",

                    "CREATE TABLE IF NOT EXISTS learn_history (" +
                    "    id TEXT PRIMARY KEY NOT NULL," +
                    "    monday INTEGER DEFAULT 0," +
                    "    tuesday INTEGER DEFAULT 0," +
                    "    wednesday INTEGER DEFAULT 0," +
                    "    thursday INTEGER DEFAULT 0," +
                    "    friday INTEGER DEFAULT 0," +
                    "    saturday INTEGER DEFAULT 0," +
                    "    sunday INTEGER DEFAULT 0," +
                    "    streak INTEGER DEFAULT 0," +
                    "    updated_at TEXT," +
                    "    created_at TEXT," +
                    "    is_synced INTEGER DEFAULT 0" +
                    ");",

                    "CREATE TABLE IF NOT EXISTS vocabulary (" +
                    "    id TEXT PRIMARY KEY NOT NULL," +
                    "    user_id TEXT NOT NULL," +
                    "    word TEXT NOT NULL," +
                    "    meaning TEXT NOT NULL," +
                    "    ipa TEXT," +
                    "    type TEXT," +
                    "    example TEXT," +
                    "    note TEXT," +
                    "    repetition INTEGER DEFAULT 0," +
                    "    interval INTEGER DEFAULT 0," +
                    "    ease_factor REAL DEFAULT 2.5," +
                    "    next_review_at TEXT," +
                    "    created_at TEXT," +
                    "    updated_at TEXT," +
                    "    deleted_at TEXT," +
                    "    is_synced INTEGER DEFAULT 0" +
                    ");",

                    "CREATE TABLE IF NOT EXISTS todo_list (" +
                    "    id TEXT PRIMARY KEY NOT NULL," +
                    "    user_id TEXT NOT NULL," +
                    "    content TEXT NOT NULL," +
                    "    is_finished INTEGER DEFAULT 0," +
                    "    deadline TEXT," +
                    "    created_at TEXT," +
                    "    updated_at TEXT," +
                    "    deleted_at TEXT," +
                    "    is_synced INTEGER DEFAULT 0" +
                    ");",

                    "CREATE TABLE IF NOT EXISTS sync_metadata (" +
                    "    user_id TEXT NOT NULL," +
                    "    table_name TEXT NOT NULL," +
                    "    last_sync_time TEXT," +
                    "    PRIMARY KEY (user_id, table_name)" +
                    ");",

                    "CREATE TABLE IF NOT EXISTS cache_data (" +
                    "    key TEXT PRIMARY KEY  NOT NULL," +
                    "    value TEXT," +
                    "    updated_at TEXT" +
                    ");"
                };

                for (String sql : sqlCommands) {
                    st.execute(sql);
                }
                System.out.println("Database đã được khởi tạo thành công.");
           } catch (SQLException e) {
               e.printStackTrace();
           }
        });
    }

    public void executeWrite(Runnable task) {
        executor.execute(task);
    }

    /**
     * Gửi tác vụ ghi vào hàng đợi và CHỜ kết quả trả về.
     * Dùng cho các thao tác write cần biết kết quả (boolean) để Service xử lý tiếp.
     * Vẫn đảm bảo chỉ 1 luồng ghi tại một thời điểm.
     */
    public <T> T executeWriteWithResult(Callable<T> task) {
        try {
            return executor.submit(task).get();
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
            return null;
        }
    }

    public void closeDB() {
        if(executor != null && !executor.isShutdown()) {
            executor.shutdown();
        }
    }
}
