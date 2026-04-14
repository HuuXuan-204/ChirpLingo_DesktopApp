package com.chirplingo.repository.impls;

import com.chirplingo.repository.base.BaseRepository;
import com.chirplingo.repository.interfaces.SyncMetaDataRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.OffsetDateTime;

import com.chirplingo.domain.SyncMetaData;

public class SyncMetaDataRepo extends BaseRepository<SyncMetaData> implements SyncMetaDataRepository{
    public SyncMetaDataRepo() {
        super("sync_metadata");
    }

    @Override
    public OffsetDateTime getLastSyncTime(String table) {
        String userId = getUserId();
        if(userId == null) return null;

        String sql = "SELECT * FROM sync_metadata WHERE user_id = ? AND table_name = ?";
        SyncMetaData syncMetaData = queryFirst(sql, userId, table);

        if (syncMetaData == null) return null;

        return syncMetaData.getLastSyncTime();
    }

    @Override
    protected SyncMetaData mapRow(ResultSet rs) {
        try {
            String userId = rs.getString("user_id");
            String tableName = rs.getString("table_name");
            OffsetDateTime lastSyncTime = rs.getString("last_sync_time") != null ? OffsetDateTime.parse(rs.getString("last_sync_time")) : null;

            return new SyncMetaData(userId, tableName, lastSyncTime);

        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi map row cho bảng sync_metadata", e);
        }
    }

    @Override
    protected String getUpsertSql() {
        return "INSERT INTO " + tableName + " (user_id, table_name, last_sync_time) " +
               "VALUES (?, ?, ?) " +
               "ON CONFLICT(user_id, table_name) DO UPDATE SET " +
               "last_sync_time = EXCLUDED.last_sync_time";
    }

    @Override
    public String getSelectSql() {
        return "SELECT * FROM " + tableName + " WHERE user_id = ?";

    }

    @Override
    public Object[] getUpsertParams(SyncMetaData entity) {
        return new Object[] {
            entity.getUserId(),
            entity.getTableName(),
            entity.getLastSyncTime() != null ? entity.getLastSyncTime().toString() : null,
        };
    } 
}
