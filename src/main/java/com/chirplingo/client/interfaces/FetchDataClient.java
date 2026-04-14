package com.chirplingo.client.interfaces;

import java.util.List;

public interface FetchDataClient<T> {
    /**
     * Lấy dữ liệu mới nhất
     * @return Dữ liệu mới nhất
     */
    public T fetchLatest();
    
    /**
     * Lấy tất cả dữ liệu
     * @return Tất cả dữ liệu
     */
    public List<T> fetchAll();
}
