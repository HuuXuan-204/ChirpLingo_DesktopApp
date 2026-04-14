package com.chirplingo.repository.impls;

import com.chirplingo.repository.interfaces.CacheDataRepository;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

import com.chirplingo.repository.base.BaseRepository;
import com.chirplingo.domain.CacheData;

public class CacheDataRepo extends BaseRepository<CacheData> implements CacheDataRepository {
    public CacheDataRepo() {
        super("cache_data");
    }

    @Override
    public CacheData getValidData(String key) {
        String sql = "SELECT * FROM " + tableName + " WHERE key = ?";

        CacheData cacheData = queryFirst(sql, key);
        if (cacheData != null && cacheData.getUpdatedAt() != null && cacheData.getUpdatedAt().equals(LocalDate.now())) {
            return cacheData;
        } 

        return null;

    }

    @Override
    public CacheData mapRow(ResultSet rs) {
        try {
            String key = rs.getString("key");
            String value = rs.getString("value");
            LocalDate updatedAt = rs.getString("updated_at") != null ? LocalDate.parse(rs.getString("updated_at")) : null;

            return new CacheData(key, value, updatedAt);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi khi map row sang CacheData Entity");
        }
    }

    @Override
    protected String getUpsertSql() {
        return "INSERT INTO " + tableName + " (key, value, updated_at) " +
               "VALUES (?, ?, ?) " +
               "ON CONFLICT(key) DO UPDATE SET " +
               "value = EXCLUDED.value, " +
               "updated_at = EXCLUDED.updated_at";
    }

    @Override
    public Object[] getUpsertParams(CacheData entity) {
        return new Object[] {
            entity.getKey(),
            entity.getValue(),
            entity.getUpdatedAt()
        };
    }

    

    @Override
    public String getSelectSql() {
        return "SELECT"; // Không dùng
    }
}
