package com.chirplingo.service.interfaces;

public interface NetworkService {

    /**
     * Kiểm tra xem có mạng hay không
     * @return true nếu có mạng, false nếu không có mạng
     */
    public boolean isNetworkAvailable();

    /**
     * Thêm listener để nhận thông báo khi có thay đổi về trạng thái mạng
     * @param listener Listener để nhận thông báo
     */
    public void addNetworkListener(NetworkListener listener);

    /**
     * Xóa listener
     * @param listener Listener cần xóa
     */
    public void removeNetworkListener(NetworkListener listener);
    
}
