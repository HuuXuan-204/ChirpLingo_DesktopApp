package com.chirplingo.auth;

import com.fasterxml.jackson.databind.ObjectMapper;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;


public class TokenManager {

    private static final String APP_DIR_NAME = "ChirpLingo";
    private static final String SESSION_FILE = "session.dat";
    private static final String CIPHER_ALGO = "AES/CBC/PKCS5Padding";

    private final File sessionFile;
    private final ObjectMapper mapper;
    private final SecretKey secretKey;
    private final IvParameterSpec iv;

    private static TokenManager instance;

    public static synchronized TokenManager getInstance() {
        if (instance == null) {
            instance = new TokenManager();
        }
        return instance;
    }

    private TokenManager() {
        this.mapper = new ObjectMapper();

        String appData = System.getenv("LOCALAPPDATA");
        if (appData == null) appData = System.getProperty("user.home");
        File appDir = new File(appData, APP_DIR_NAME);
        appDir.mkdirs(); 
        this.sessionFile = new File(appDir, SESSION_FILE);

        this.secretKey = deriveKey();
        this.iv = deriveIv();
    }

    public void saveSession(AuthSession session) {
        if (session == null || !session.isValid()) return;
        try {
            String json = mapper.writeValueAsString(session);
            String encrypted = encrypt(json);
            Files.writeString(sessionFile.toPath(), encrypted, StandardCharsets.UTF_8);
        } catch (Exception e) {
            System.err.println("TokenManager: Lỗi khi lưu session - " + e.getMessage());
        }
    }

    public AuthSession loadSession() {
        if (!sessionFile.exists()) return null;
        try {
            String encrypted = Files.readString(sessionFile.toPath(), StandardCharsets.UTF_8);
            String json = decrypt(encrypted);
            AuthSession session = mapper.readValue(json, AuthSession.class);
            return session.isValid() ? session : null;
        } catch (Exception e) {
            System.err.println("TokenManager: Không thể đọc session, có thể đã bị hỏng - " + e.getMessage());
            clearSession(); 
            return null;
        }
    }

    public void clearSession() {
        try {
            if (sessionFile.exists()) {
                sessionFile.delete();
            }
        } catch (Exception e) {
            System.err.println("TokenManager: Lỗi khi xóa session - " + e.getMessage());
        }
    }

    public boolean hasSession() {
        return sessionFile.exists() && sessionFile.length() > 0;
    }

    private String encrypt(String plainText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.ENCRYPT_MODE, secretKey, iv);
        byte[] encrypted = cipher.doFinal(plainText.getBytes(StandardCharsets.UTF_8));
        return Base64.getEncoder().encodeToString(encrypted);
    }

    private String decrypt(String cipherText) throws Exception {
        Cipher cipher = Cipher.getInstance(CIPHER_ALGO);
        cipher.init(Cipher.DECRYPT_MODE, secretKey, iv);
        byte[] decrypted = cipher.doFinal(Base64.getDecoder().decode(cipherText));
        return new String(decrypted, StandardCharsets.UTF_8);
    }

    private SecretKey deriveKey() {
        try {
            String hostname = java.net.InetAddress.getLocalHost().getHostName();
            String username = System.getProperty("user.name");
            String seed = hostname + "::" + username + "::chirplingo";

            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(seed.getBytes(StandardCharsets.UTF_8));
            byte[] key = Arrays.copyOf(hash, 16); 
            return new SecretKeySpec(key, "AES");
        } catch (Exception e) {
            byte[] fallback = "ChirpLingoSecKey".getBytes(StandardCharsets.UTF_8);
            return new SecretKeySpec(fallback, "AES");
        }
    }

    private IvParameterSpec deriveIv() {
        try {
            String username = System.getProperty("user.name") + "::iv::chirplingo";
            MessageDigest sha = MessageDigest.getInstance("SHA-256");
            byte[] hash = sha.digest(username.getBytes(StandardCharsets.UTF_8));
            byte[] ivBytes = Arrays.copyOf(hash, 16);
            return new IvParameterSpec(ivBytes);
        } catch (Exception e) {
            byte[] fallback = "ChirpLingoIV1234".getBytes(StandardCharsets.UTF_8);
            return new IvParameterSpec(fallback);
        }
    }
}
