package com.chirplingo.repository.interfaces;

import java.time.OffsetDateTime;

public interface SyncMetaDataRepository {
    
    /**------------------------------------------------------------------
     * Lấy thời gian sync cuối cùng của 1 bảng (Bảng profile, vocabularies, learn_history, todolist)
     * @param tableName Tên bảng
     * @return Thời gian sync cuối cùng
     */
    public OffsetDateTime getLastSyncTime(String tableName);

    /**
     * Lưu thời gian sync cuối cùng của 1 bảng
     * @param tableName Tên bảng
     * @param lastSyncTime Thời gian sync cuối cùng
     * @return true nếu lưu thành công, false nếu lưu thất bại
     */
    public boolean saveLastSyncTime(String tableName, OffsetDateTime lastSyncTime);

}
