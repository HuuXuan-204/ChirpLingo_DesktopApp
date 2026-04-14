package com.chirplingo.repository.interfaces;

import java.time.OffsetDateTime;

public interface SyncMetaDataRepository {
    
    /**------------------------------------------------------------------
     * Lấy thời gian sync cuối cùng của 1 bảng (Bảng profile, vocabularies, learn_history, todolist)
     * @param tableName Tên bảng
     * @return Thời gian sync cuối cùng
     */
    public OffsetDateTime getLastSyncTime(String tableName);

}
