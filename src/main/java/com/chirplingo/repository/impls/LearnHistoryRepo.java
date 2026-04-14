package com.chirplingo.repository.impls;

import com.chirplingo.repository.interfaces.LearnHistoryRepository;
import com.chirplingo.domain.LearnHistory;
import com.chirplingo.repository.base.BaseRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class LearnHistoryRepo extends BaseRepository<LearnHistory> implements LearnHistoryRepository {
    public LearnHistoryRepo() {
        super("learn_history");
    }

    @Override
    protected LearnHistory mapRow(ResultSet rs) {
        try {
            String id = rs.getString("id");
            OffsetDateTime createdAt = rs.getString("created_at") != null ? OffsetDateTime.parse(rs.getString("created_at")) : null;
            OffsetDateTime updatedAt = rs.getString("updated_at") != null ? OffsetDateTime.parse(rs.getString("updated_at")) : null;
            boolean isSynced = rs.getInt("is_synced") == 1;
            int monday = rs.getInt("monday");
            int tuesday = rs.getInt("tuesday");
            int wednesday = rs.getInt("wednesday");
            int thursday = rs.getInt("thursday");
            int friday = rs.getInt("friday");
            int saturday = rs.getInt("saturday");
            int sunday = rs.getInt("sunday");   
            int streak = rs.getInt("streak");
            
            return new LearnHistory(id, createdAt, updatedAt, isSynced, monday, tuesday, wednesday, thursday, friday, saturday, sunday, streak);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi map row sang LearnHistory Entity");
        }
    }

    @Override
    protected String getUpsertSql() {
        return "INSERT INTO " + tableName + " (id, monday, tuesday, wednesday, thursday, friday, saturday, sunday, streak, updated_at, created_at, is_synced) " + 
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
               "ON CONFLICT(id) DO UPDATE SET " +
               "monday = EXCLUDED.monday, " +
               "tuesday = EXCLUDED.tuesday, " +
               "wednesday = EXCLUDED.wednesday, " +
               "thursday = EXCLUDED.thursday, " +
               "friday = EXCLUDED.friday, " +
               "saturday = EXCLUDED.saturday, " +
               "sunday = EXCLUDED.sunday, " +
               "streak = EXCLUDED.streak, " +
               "updated_at = EXCLUDED.updated_at, " +
               "is_synced = EXCLUDED.is_synced";
    }

    @Override
    protected String getSelectSql() {
        return "SELECT * FROM " + tableName + " WHERE id = ? AND deleted_at IS NULL";
    }

    @Override
    protected Object[] getUpsertParams(LearnHistory entity) {
        return new Object[] {
            entity.getId(),
            entity.getDayValue(0),
            entity.getDayValue(1),
            entity.getDayValue(2),
            entity.getDayValue(3),
            entity.getDayValue(4),
            entity.getDayValue(5),
            entity.getDayValue(6),
            entity.getStreak(),
            entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null,
            entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
            entity.isSynced() ? 1 : 0
        };
    }
}

