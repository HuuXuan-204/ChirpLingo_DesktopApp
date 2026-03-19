package com.chirplingo.domain.base;

import java.time.OffsetDateTime;

/**
 * Interface dành cho các đối tượng cần đồng bộ dữ liệu với server
 */
public interface Syncable extends Identifiable {
    /**
     * @return Thời điểm cập nhật gần nhất của đối tượng
     */
    public OffsetDateTime getUpdatedAt();

    /**
     * Kiểm tra trạng thái đồng bộ dữ liệu
     * @return true nếu đã được đồng bộ với server và ngược lại là false
     */
    public boolean isSynced();

    /**
     * Đánh dấu đối tượng này đã được đồng bộ
     */
    public void markAsSynced();

    /**
     * Đánh dấu đối tượng này cần đồng bộ lại (Trước đó có thể được update, insert, softDelete)
     */
    public void markAsUnsynced();
}
