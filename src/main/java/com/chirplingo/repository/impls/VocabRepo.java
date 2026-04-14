package com.chirplingo.repository.impls;

import com.chirplingo.domain.Vocabulary;
import com.chirplingo.repository.base.BaseRepository;
import com.chirplingo.repository.interfaces.VocabRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;
import java.util.List;
import java.util.ArrayList;
import com.chirplingo.utils.CommonUtils;

public class VocabRepo extends BaseRepository<Vocabulary> implements VocabRepository{
    public VocabRepo() {
        super("vocabulary");
    }
    
    @Override
    public List<Vocabulary> getDue() {
        String userId = getUserId();
        if(userId == null) return new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND deleted_at IS NULL AND next_review_at <= ?";
        OffsetDateTime now = CommonUtils.getOffsetDateTime();
        return queryList(sql, userId, now.toString());
    }

    @Override
    public int countDue() {
        String userId = getUserId();
        if(userId == null) return 0;
        String sql = "SELECT COUNT(*) FROM " + tableName + " WHERE user_id = ? AND deleted_at IS NULL AND next_review_at <= ?";
        OffsetDateTime now = CommonUtils.getOffsetDateTime();
        return queryCount(sql, userId, now.toString());
    }

    @Override
    public List<Vocabulary> getRandom(int number) {
        String userId = getUserId();
        if(userId == null) return new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND deleted_at IS NULL ORDER BY RANDOM() LIMIT ?";
        return queryList(sql, userId, number);
    }

    @Override
    public List<Vocabulary> searchVocabs(String keyword) {
        String userId = getUserId();
        if(userId == null || keyword == null || keyword.trim().isEmpty()) return new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND deleted_at IS NULL AND (word LIKE ? OR meaning LIKE ?) ORDER BY word ASC LIMIT 50";
        return queryList(sql, userId, "%" + keyword.trim() + "%", "%" + keyword.trim() + "%");
    }
    
    protected Vocabulary mapRow(ResultSet rs) {
        try {
            String id = rs.getString("id");
            OffsetDateTime createdAt = rs.getString("created_at") != null ? OffsetDateTime.parse(rs.getString("created_at")) : null;
            OffsetDateTime updatedAt = rs.getString("updated_at") != null ? OffsetDateTime.parse(rs.getString("updated_at")) : null;
            boolean isSynced = rs.getInt("is_synced") == 1;
            String word = rs.getString("word");
            String meaning = rs.getString("meaning");
            String ipa = rs.getString("ipa");
            String type = rs.getString("type");
            String example = rs.getString("example");
            String note = rs.getString("note");
            int repetition = rs.getInt("repetition");
            int interval = rs.getInt("interval");
            double easeFactor = rs.getDouble("ease_factor");
            OffsetDateTime nextReviewAt = rs.getString("next_review_at") != null ? OffsetDateTime.parse(rs.getString("next_review_at")) : null;
            String userId = rs.getString("user_id");
            OffsetDateTime deletedAt = rs.getString("deleted_at") != null ? OffsetDateTime.parse(rs.getString("deleted_at")) : null;

            Vocabulary vocab = new Vocabulary(id, createdAt, updatedAt, isSynced, word, ipa, type, meaning, example, note, userId, nextReviewAt, interval, easeFactor, repetition, deletedAt);
            return vocab;
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Lỗi khi mapRow sang Vocabulary Entity");
        }
        return null;
    }   

    protected String getUpsertSql() {
        return "INSERT INTO " + tableName + " (id, user_id, word, meaning, ipa, type, example, note, repetition, interval, ease_factor, next_review_at, created_at, updated_at, deleted_at, is_synced) " +
               "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?) " +
               "ON CONFLICT(id) DO UPDATE SET " +
               "word = EXCLUDED.word, " +
               "meaning = EXCLUDED.meaning, " +
               "ipa = EXCLUDED.ipa, " +
               "type = EXCLUDED.type, " +
               "example = EXCLUDED.example, " +
               "note = EXCLUDED.note, " +
               "repetition = EXCLUDED.repetition, " +
               "interval = EXCLUDED.interval, " +
               "ease_factor = EXCLUDED.ease_factor, " +
               "next_review_at = EXCLUDED.next_review_at, " +
               "updated_at = EXCLUDED.updated_at, " +
               "deleted_at = EXCLUDED.deleted_at, " +
               "is_synced = EXCLUDED.is_synced";
    }

    protected String getSelectSql() {
        return "SELECT * FROM " + tableName + " WHERE user_id = ? AND deleted_at IS NULL";
    }

    protected Object[] getUpsertParams(Vocabulary entity) {
        return new Object[] {
            entity.getId(),
            entity.getUserId(),
            entity.getWord(),
            entity.getMeaning(),
            entity.getIpa(),
            entity.getType(),
            entity.getExample(),
            entity.getNote(),
            entity.getRepetition(),
            entity.getInterval(),
            entity.getEaseFactor(),
            entity.getNextReviewAt() != null ? entity.getNextReviewAt().toString() : null,
            entity.getCreatedAt() != null ? entity.getCreatedAt().toString() : null,
            entity.getUpdatedAt() != null ? entity.getUpdatedAt().toString() : null,
            entity.getDeletedAt() != null ? entity.getDeletedAt().toString() : null,
            entity.isSynced() ? 1 : 0
        };
    }
}
