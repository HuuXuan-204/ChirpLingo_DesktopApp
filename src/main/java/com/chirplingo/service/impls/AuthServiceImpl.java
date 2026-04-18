package com.chirplingo.service.impls;

import com.chirplingo.auth.AuthSession;
import com.chirplingo.auth.TokenManager;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.domain.UserSession;
import com.chirplingo.repository.interfaces.ProfileRepository;
import com.chirplingo.service.interfaces.AuthService;
import com.chirplingo.service.interfaces.NetworkService;
import com.chirplingo.service.interfaces.SyncService;
import com.fasterxml.jackson.databind.JsonNode;

import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.HashMap;
import java.util.Map;
import com.chirplingo.utils.Config;
import com.chirplingo.utils.ValidationUtils;
import com.chirplingo.domain.Result;

public class AuthServiceImpl implements AuthService {

    private final SupabaseContext context;
    private final UserSession userSession;
    private final ProfileRepository profileRepo;
    private final NetworkService networkService;
    private final SyncService syncService;
    private final TokenManager tokenManager;

    public AuthServiceImpl(SupabaseContext context, ProfileRepository profileRepo, NetworkService networkService, SyncService syncService) {
        this.context = context;
        this.profileRepo = profileRepo;
        this.networkService = networkService;
        this.syncService = syncService;
        this.userSession = UserSession.getInstance();
        this.tokenManager = TokenManager.getInstance();
    }

    @Override
    public Result login(String email, String password) {
        Result validation = ValidationUtils.begin()
                .email(email)
                .password(password)
                .get();
        if (!validation.isSuccess()) return validation;

        if (!networkService.isNetworkAvailable()) {
            return Result.fail("Tín hiệu từ tổ Cú đang bị nhiễu sóng rồi đằng ấy ơi! Kiểm tra lại mạng để mình nghe rõ lời bạn nói nhé. Chirp chirp!", "NO_INTERNET");
        }

        try { 
            Map<String, String> body = new HashMap<>();
            body.put("email", email);
            body.put("password", password);

            HttpRequest request = buildAuthRequest("/auth/v1/token?grant_type=password")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            context.getMapper().writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = context.getHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status == 200) {
                AuthSession session = parseSession(response.body());
                if(session != null) {
                    tokenManager.saveSession(session);
                    context.setAccessToken(session.getAccessToken());
                    userSession.setUserId(session.getUserId());
                    
                    // new Thread(syncService::syncAll).start();
                    
                    return Result.success("Chào mừng quay trở lại! Vào chiến thôi, kiến thức đang chờ đằng ấy đó.");
                }
            } else if (status >= 400 && status < 500) {
                JsonNode errorNode = context.getMapper().readTree(response.body());
                String error = textOrNull(errorNode.path("error"));
                String errorDescription = textOrNull(errorNode.path("error_description"));
                
                if ("invalid_grant".equals(error)) {
                    return Result.fail("Email hoặc mật khẩu không chính xác rồi bạn ơi. Thử lại nhé!", "INVALID_CREDENTIALS");
                } else if ("email_not_confirmed".equals(error) || (errorDescription != null && errorDescription.contains("Email not confirmed"))) {
                    return Result.fail("Bạn ơi, hình như email này chưa được xác nhận. Kiểm tra hộp thư đến xem sao nhé!", "EMAIL_NOT_CONFIRMED");
                }
                
                return Result.fail("Đăng nhập không thành công: " + (errorDescription != null ? errorDescription : "Lỗi xác thực."), "AUTH_ERROR");
            }

            System.err.println("Login thất bại [" + status + "]: " + response.body());
            return Result.fail("Có lỗi từ máy chủ (Mã: " + status + "). Hãy thử lại sau nhé!", "SERVER_ERROR");

        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Hệ thống gặp chút trục trặc: " + e.getMessage(), "SYSTEM_EXCEPTION");
        }
    }

    @Override
    public Result register(String email, String password, String userName) {
        Result validation = ValidationUtils.begin()
                .notBlank(userName, "tên người dùng")
                .email(email)
                .password(password)
                .get();
        if (!validation.isSuccess()) return validation;

        if (!networkService.isNetworkAvailable()) return Result.fail("Tín hiệu từ tổ Cú đang bị nhiễu sóng rồi đằng ấy ơi! Kiểm tra lại mạng để mình nghe rõ lời bạn nói nhé. Chirp chirp!", "NO_INTERNET");

        try {
            Map<String, Object> body = new HashMap<>();
            body.put("email", email);
            body.put("password", password);
            body.put("data", Map.of("user_name", userName));

            String endpoint = "/auth/v1/signup";

            HttpRequest request = buildAuthRequest(endpoint)
                    .POST(HttpRequest.BodyPublishers.ofString(
                            context.getMapper().writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = context.getHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status == 200 || status == 201) {
                return Result.success("Đăng ký thành công! Hãy vào hòm thư email để xác thực tài khoản trước khi đăng nhập bạn nhé.");
            } else if (status >= 400 && status < 500) {
                JsonNode errorNode = context.getMapper().readTree(response.body());
                String error = textOrNull(errorNode.path("error"));
                String errorDescription = textOrNull(errorNode.path("error_description"));
                
                if ("email_already_in_use".equals(error) || "user_already_exists".equals(error)) {
                    return Result.fail("Email này đã có người sử dụng rồi bạn ơi. Thử email khác nhé!", "EMAIL_ALREADY_IN_USE");
                }
                
                return Result.fail("Đăng ký không thành công: " + (errorDescription != null ? errorDescription : "Lỗi xác thực."), "AUTH_ERROR");
            }

            System.err.println("Register thất bại [" + status + "]: " + response.body());
            return Result.fail("Có lỗi từ máy chủ (Mã: " + status + "). Hãy thử lại sau nhé!", "SERVER_ERROR");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Hệ thống gặp chút trục trặc: " + e.getMessage(), "SYSTEM_EXCEPTION");
        }
    }

    @Override
    public boolean logout() {
        clearSession();
        try {
            HttpRequest request = buildAuthorizedRequest("/auth/v1/logout")
                    .POST(HttpRequest.BodyPublishers.noBody())
                    .build();
            context.getHttpClient().send(request, HttpResponse.BodyHandlers.discarding());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    public boolean tryAutoLogin() {
        AuthSession saved = tokenManager.loadSession();
        if (saved == null || !saved.isValid()) return false;

        if (!saved.isExpired()) {
            context.setAccessToken(saved.getAccessToken());
            userSession.setUserId(saved.getUserId());
            return true;
        }
        if (networkService.isNetworkAvailable()) {
            return refreshSession(saved);
        }
        userSession.setUserId(saved.getUserId());
        return true;
    }

    private boolean refreshSession(AuthSession session) {
        if (session == null || session.getRefreshToken() == null) return false;
        if (!networkService.isNetworkAvailable()) return false;
        try {
            Map<String, String> body = Map.of("refresh_token", session.getRefreshToken());

            HttpRequest request = buildAuthRequest("/auth/v1/token?grant_type=refresh_token")
                    .POST(HttpRequest.BodyPublishers.ofString(
                            context.getMapper().writeValueAsString(body)))
                    .build();

            HttpResponse<String> response = context.getHttpClient()
                    .send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();
            if (status == 200) {
                AuthSession newSession = parseSession(response.body());
                tokenManager.saveSession(newSession);
                context.setAccessToken(newSession.getAccessToken());
                userSession.setUserId(newSession.getUserId());
                return true;
            }
            if (status >= 400 && status < 500) {
                clearSession(); 
                System.err.println("Phiên đăng nhập đã hết hạn");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public Result changeEmail(String newEmail) {
        Result validation = ValidationUtils.begin().email(newEmail).get();
        if (!validation.isSuccess()) return validation;
        if (!isLoggedIn()) return Result.fail("Bạn cần đăng nhập để đổi email nhé", "AUTH_REQUIRED");
        if (!networkService.isNetworkAvailable()) return Result.fail("Tín hiệu từ tổ Cú đang bị nhiễu sóng rồi đằng ấy ơi! Kiểm tra lại mạng để mình nghe rõ lời bạn nói nhé. Chirp chirp!", "NO_INTERNET");
        
        try {
            Map<String, String> body = Map.of("email", newEmail);
            HttpRequest request = buildAuthorizedRequest("/auth/v1/user")
                    .method("PUT", HttpRequest.BodyPublishers.ofString(
                            context.getMapper().writeValueAsString(body)))
                    .build();
            context.getHttpClient().send(request, HttpResponse.BodyHandlers.discarding());
            return Result.success("Kiểm tra hòm thư email cũ và mới để xác nhận thay đổi nhé!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Lỗi khi gửi yêu cầu đổi email: " + e.getMessage(), "SYSTEM_EXCEPTION");
        }
    }

    @Override
    public Result resetPassword(String email) {
        Result validation = ValidationUtils.begin().email(email).get();
        if (!validation.isSuccess()) return validation;
        if (!networkService.isNetworkAvailable()) return Result.fail("Tín hiệu từ tổ Cú đang bị nhiễu sóng rồi đằng ấy ơi! Kiểm tra lại mạng nhé.", "NO_INTERNET");
        
        try {
            Map<String, String> body = new HashMap<>();
            body.put("email", email);
            String redirectTo = Config.get("AUTH_REDIRECT_RESET");
            if (redirectTo != null && !redirectTo.isBlank()) {
                body.put("redirectTo", redirectTo);     
            }
            String endpoint = "/auth/v1/recover";

            HttpRequest request = buildAuthRequest(endpoint)
                    .POST(HttpRequest.BodyPublishers.ofString(
                            context.getMapper().writeValueAsString(body)))
                    .build();
            context.getHttpClient().send(request, HttpResponse.BodyHandlers.discarding());
            return Result.success("Link đặt lại mật khẩu đã được gửi vào email của bạn!");
        } catch (Exception e) {
            e.printStackTrace();
            return Result.fail("Lỗi hệ thống: " + e.getMessage(), "SYSTEM_EXCEPTION");
        }
    }

    @Override
    public boolean isLoggedIn() {
        return userSession.isActive();
    }

    @Override
    public String getCurrentUserId() {
        return userSession.getUserId();
    }

    private AuthSession parseSession(String responseBody) {
        try {
            JsonNode root = context.getMapper().readTree(responseBody);
            String accessToken = textOrNull(root.path("access_token"));
            String refreshToken = textOrNull(root.path("refresh_token"));
            long expiresIn = root.path("expires_in").asLong(3600);
            String userId = textOrNull(root.path("user").path("id"));
            String email = textOrNull(root.path("user").path("email"));

            if (accessToken == null || userId == null) {
                return null;
            }
            return new AuthSession(accessToken, refreshToken, expiresIn, userId, email);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    private String textOrNull(JsonNode node) {
        return (node == null || node.isNull() || node.isMissingNode()) ? null : node.asText();
    }

    private void clearSession() {
        userSession.clear();
        context.setAccessToken(null);
        tokenManager.clearSession();
    }

    private HttpRequest.Builder buildAuthRequest(String endpoint) {
        return HttpRequest.newBuilder()
                .uri(URI.create(context.getUrl() + endpoint))
                .header("apikey", context.getAnonKey())
                .header("Content-Type", "application/json");
    }

    private HttpRequest.Builder buildAuthorizedRequest(String endpoint) {
        return buildAuthRequest(endpoint)
                .header("Authorization", "Bearer " + context.getAccessToken());
    }
}
