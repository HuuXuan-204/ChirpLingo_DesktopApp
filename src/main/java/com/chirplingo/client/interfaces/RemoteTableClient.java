package com.chirplingo.client.interfaces;

import java.util.List;

public interface RemoteTableClient<T> {
    /**
     * Đẩy dữ liệu chưa đồng bộ lên server
     * @param items Danh sách dữ liệu chưa đồng bộ
     * @return true nếu đẩy thành công, false nếu thất bại
     */
    public boolean pushUnsynced(List<T> items);
    
    /**
     * Lấy dữ liệu mới từ trên server 
     * @return Danh sách dữ liệu mới
     */
    public List<T> pullChanges();
}
