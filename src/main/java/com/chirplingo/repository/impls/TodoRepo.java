package com.chirplingo.repository.impls;

import com.chirplingo.domain.TodoItem;
import com.chirplingo.repository.base.BaseRepository;
import com.chirplingo.repository.interfaces.TodoRepository;
import java.util.List;
import java.util.ArrayList;
import com.chirplingo.utils.CommonUtils;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.OffsetDateTime;
import java.time.ZoneId;
import java.time.ZoneOffset;

public class TodoRepo extends BaseRepository<TodoItem> implements TodoRepository {
    public TodoRepo() {
        super("todo_list");
    }

    @Override
    public List<TodoItem> getToday() {
        String userId = getUserId();
        if (userId == null) return new ArrayList<>();

        LocalDate today = CommonUtils.getLocalDate();
        String startDay = today.atStartOfDay(ZoneId.systemDefault())
                       .withZoneSameInstant(ZoneOffset.UTC)
                       .toOffsetDateTime().toString();
        String endDay = today.atTime(LocalTime.MAX).atZone(ZoneId.systemDefault())
                     .withZoneSameInstant(ZoneOffset.UTC)
                     .toOffsetDateTime().toString();

        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND is_finished = 0 AND deleted_at IS NULL AND deadline BETWEEN ? AND ?";
        return queryList(sql, userId, startDay, endDay);
    }

    @Override
    public List<TodoItem> getUpcoming() {
        String userId = getUserId();
        if (userId == null) return new ArrayList<>();

        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND is_finished = 0 AND deleted_at IS NULL AND deadline > ? ORDER BY deadline ASC";
        return queryList(sql, userId, CommonUtils.getOffsetDateTime().toString());
    }

    @Override
    public List<TodoItem> getOverDue() {
        String userId = getUserId();
        if (userId == null) return new ArrayList<>();

        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND is_finished = 0 AND deleted_at IS NULL AND deadline < ? ORDER BY deadline DESC";
        return queryList(sql, userId, CommonUtils.getOffsetDateTime().toString());
    }

    @Override
    public List<TodoItem> getCompleted() {
        String userId = getUserId();
        if (userId == null) return new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND is_finished = 1 AND deleted_at IS NULL ORDER BY updated_at DESC";
        return queryList(sql, userId);
    }

    @Override
    protected TodoItem mapRow(ResultSet rs) {
        try {
            String id = rs.getString("id");
            OffsetDateTime createdAt = rs.getString("created_at") != null ? OffsetDateTime.parse(rs.getString("created_at")) : null;
            OffsetDateTime updatedAt = rs.getString("updated_at") != null ? OffsetDateTime.parse(rs.getString("updated_at")) : null;
            boolean isSynced = rs.getInt("is_synced") == 1;
            String content = rs.getString("content");
            boolean isFinished = rs.getInt("is_finished") == 1;
            OffsetDateTime deadline = rs.getString("deadline") != null ? OffsetDateTime.parse(rs.getString("deadline")) : null;
            String userId = rs.getString("user_id");
            OffsetDateTime deletedAt = rs.getString("deleted_at") != null ? OffsetDateTime.parse(rs.getString("deleted_at")) : null;

            return new TodoItem(id, createdAt, updatedAt, isSynced, content, isFinished, deadline, userId, deletedAt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi map row cho bảng todo_list", e);
        }
    }

    @Override
    protected String getUpsertSql() {
        return "INSERT INTO " + tableName + " (id, user_id, content, is_finished, deadline, created_at, updated_at, deleted_at, is_synced) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?) " +
               "ON CONFLICT(id) DO UPDATE SET " +
               "content = EXCLUDED.content, " +
               "is_finished = EXCLUDED.is_finished, " +
               "deadline = EXCLUDED.deadline, " +
               "updated_at = EXCLUDED.updated_at, " +
               "deleted_at = EXCLUDED.deleted_at, " +
               "is_synced = EXCLUDED.is_synced";
    }

    @Override
    protected String getSelectSql() {
        return "SELECT * FROM " + tableName + " WHERE user_id = ? AND deleted_at IS NULL";
    }

    @Override
    protected Object[] getUpsertParams(TodoItem entity) {
        return new Object[] {
            entity.getId(),
            entity.getUserId(),
            entity.getContent(),
            entity.isFinished() ? 1 : 0,
            entity.getDeadline() != null ? entity.getDeadline().toString() : null,
            entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
            entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null,
            entity.getDeletedAt() != null ? entity.getDeletedAt().toString() : null,
            entity.isSynced() ? 1 : 0
        };
    }

   
}
