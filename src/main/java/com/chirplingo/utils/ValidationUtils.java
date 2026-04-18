package com.chirplingo.utils;

import com.chirplingo.domain.Result;

/**
 * Kiểm tra tính hợp lệ của dữ liệu đầu vào
 */
public class ValidationUtils {
    private Result error = null;

    private ValidationUtils() {
    }

    /**
     * Bắt đầu một chuỗi kiểm tra mới
     * @return Đối tượng ValidationUtils để bắt đầu xâu chuỗi
     */
    public static ValidationUtils begin() {
        return new ValidationUtils();
    }

    /**
     * Kiểm tra trường xem có bị null hoặc để trống không
     * @param value Giá trị cần kiểm tra
     * @param fieldName Tên trường (dùng trong thông báo lỗi)
     * @return Đối tượng ValidationUtils hiện tại
     */
    public ValidationUtils notBlank(String value, String fieldName) {
        if (error != null) return this;
        if (value == null || value.isBlank()) {
            error = Result.fail("Bạn quên chưa nhập " + fieldName + " kìa bạn ơi!", "MISSING_FIELDS");
        }
        return this;
    }

    /**
     * Kiểm tra định dạng Email
     * @param email Địa chỉ email cần kiểm tra
     * @return Đối tượng ValidationUtils hiện tại
     */
    public ValidationUtils email(String email) {
        if (error != null) return this;
        if (email == null || email.isBlank()) {
            error = Result.fail("Đừng quên nhập email để mình nhận diện bạn là ai nhé!", "MISSING_FIELDS");
        } else if (!email.matches("^[A-Za-z0-9+_.-]+@[A-Za-z0-9.-]+$")) {
            error = Result.fail("Email không hợp lệ rồi bạn ơi. Kiểm tra lại xem sao nhé!", "INVALID_EMAIL");
        }
        return this;
    }

    /**
     * Kiểm tra mật khẩu 
     * @param password Mật khẩu cần kiểm tra
     * @return Đối tượng ValidationUtils hiện tại
     */
    public ValidationUtils password(String password) {
        if (error != null) return this;
        if (password == null || password.isBlank()) {
            error = Result.fail("Mật khẩu không được để trống bạn nhé!", "MISSING_FIELDS");
        } else if (password.length() < 6) {
            error = Result.fail("Mật khẩu phải có ít nhất 6 ký tự bạn nhé!", "INVALID_PASSWORD");
        }
        return this;
    }

    /**
     * Kết thúc chuỗi kiểm tra và trả về lỗi đầu tiên
     * @return Đối tượng Result
     */
    public Result get() {
        return error != null ? error : Result.success("OKELA");
    }
}
