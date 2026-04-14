package com.chirplingo.repository.impls;

import com.chirplingo.domain.User;
import com.chirplingo.repository.base.BaseRepository;
import com.chirplingo.repository.interfaces.ProfileRepository;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

public class ProfileRepo extends BaseRepository<User> implements ProfileRepository {
    public ProfileRepo() {
        super("profile");
    }

    protected User mapRow(ResultSet rs) {
        try {
            String id = rs.getString("id");
            OffsetDateTime createdAt = rs.getString("created_at") != null ? OffsetDateTime.parse(rs.getString("created_at")) : null;
            OffsetDateTime updatedAt = rs.getString("updated_at") != null ? OffsetDateTime.parse(rs.getString("updated_at")) : null;
            boolean isSynced = rs.getInt("is_synced") == 1;
            String email = rs.getString("email");
            String userName = rs.getString("user_name");
            String avatar = rs.getString("avatar");
            return new User(id, createdAt, updatedAt, isSynced, email, userName, avatar);
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi map row sang User Entity");
            return null;
        }
    }

    protected String getUpsertSql() {
        return "INSERT INTO " + tableName + " (id, email, user_name, avatar, created_at, updated_at, is_synced) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?) " +
               "ON CONFLICT(id) DO UPDATE SET " +
               "email = EXCLUDED.email, " +
               "user_name = EXCLUDED.user_name, " +
               "avatar = EXCLUDED.avatar, " +
               "updated_at = EXCLUDED.updated_at, " +
               "is_synced = EXCLUDED.is_synced";
    }

    protected String getSelectSql() {
        return "SELECT * FROM " + tableName + " WHERE id = ?";
    }

    protected Object[] getUpsertParams(User entity) {
        return new Object[] {
            entity.getId(),
            entity.getEmail(),
            entity.getUserName(),
            entity.getAvatar(),
            entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
            entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null,
            entity.isSynced() ? 1 : 0
        };
    }

}
