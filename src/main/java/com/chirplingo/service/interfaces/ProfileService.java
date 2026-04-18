package com.chirplingo.service.interfaces;

import com.chirplingo.domain.User;

public interface ProfileService {
    /**
     * Lưu profile
     * @param user User cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveUserProfile(User user);

    /**
     * Lấy thông tin user
     * @return User
     */
    public User getUserProfile();
}
