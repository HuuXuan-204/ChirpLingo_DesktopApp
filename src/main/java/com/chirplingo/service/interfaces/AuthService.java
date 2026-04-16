package com.chirplingo.service.interfaces;

public interface AuthService {
    /**
     * Đăng nhập vào ứng dụng, nhận về Access Token va Resfresh Token cùng thông tin người dùng
     * @param email Email của người dùng
     * @param password Mật khẩu của người dùng
     * @return true nếu đăng nhập thành công, false nếu đăng nhập thất bại
     */
    public boolean login(String email, String password);

    /**
     * Đăng ký tài khoản mới
     * @param email Email của người dùng
     * @param password Mật khẩu của người dùng
     * @param userName Tên người dùng
     * @return true nếu đăng ký thành công, false nếu đăng ký thất bại
     */
    public boolean register(String email, String password, String userName);

    /**
     * Đăng xuất khỏi ứng dụng (Xóa hết Token trong bộ nhớ)
     * @return true nếu đăng xuất thành công, false nếu đăng xuất thất bại
     */
    public boolean logout();

    /**
     * Làm mới phiên đăng nhập (Dùng refresh token để lấy access token mới để tiếp tục duy trì đăng nhập)
     * @return true nếu làm mới phiên thành công, false nếu làm mới phiên thất bại
     */
    public boolean refreshSession();

    /**
     * Thử đăng nhập tự động, dùng khi mở app để tự động đăng nhập nếu còn refresh token)
     * @return true nếu đăng nhập tự động thành công, false nếu đăng nhập tự động thất bại
     */
    public boolean tryAutoLogin();

    /**
     * Thay đổi email (Nhận link về email mới và cũ để bấm xác nhận)
     * @param newEmail Email mới
     */
    public void changeEmail(String newEmail);

    /**
     * Quên mật khẩu (Nhận link về email để chuyển sang trang đổi mật khẩu)
     * @param email Email của người dùng
     */
    public void resetPassword(String email);

    /**
     * Kiểm tra xem người dùng đã đăng nhập hay chưa
     * @return true nếu người dùng đã đăng nhập, false nếu người dùng chưa đăng nhập
     */
    public boolean isLoggedIn();

    /**
     * Lấy ID của người dùng hiện tại
     * @return ID của người dùng hiện tại
     */
    public String getCurrentUserId();
}
