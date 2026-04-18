package com.chirplingo.service.interfaces;

public interface NetworkListener {

    /**
     * Được gọi khi trạng thái mạng thay đổi
     * @param isAvailable true nếu có mạng, false nếu không có mạng
     */
    public void onNetworkChanged(boolean isAvailable);
}
