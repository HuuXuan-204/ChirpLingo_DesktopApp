package com.chirplingo.repository.interfaces;

import com.chirplingo.domain.CacheData;

public interface CacheDataRepository {
    
    /**----------------------------------------------------------------------
     * Lấy dữ liệu cache còn hạn sử dụng trong ngày
     * @param key Key của dữ liệu cache (quote_of_day, word_of_day,...)
     * @return Dữ liệu cache còn hạn sử dụng
     */
    public CacheData getValidData(String key);
}
