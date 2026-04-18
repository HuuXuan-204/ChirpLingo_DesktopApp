package com.chirplingo.service.interfaces;

import com.chirplingo.domain.Result;


public interface SyncService {

    /**
     * Đồng bộ từ vựng lên server
     * @return true nếu đồng bộ thành công, false nếu đồng bộ thất bại
     */
    public Result syncVocabs();

    /**
     * Đồng bộ việc cần làm lên server
     * @return true nếu đồng bộ thành công, false nếu đồng bộ thất bại
     */
    public Result syncTodos();

    /**
     * Đồng bộ thông tin cá nhân lên server
     * @return true nếu đồng bộ thành công, false nếu đồng bộ thất bại
     */
    public Result syncProfile();

    /**
     * Đồng bộ lịch sử học lên server
     * @return true nếu đồng bộ thành công, false nếu đồng bộ thất bại
     */
    public Result syncLearnHistory();

    /**
     * Đồng bộ tất cả lên server
     * @return true nếu đồng bộ thành công, false nếu đồng bộ thất bại
     */
    public Result syncAll();
    
}
