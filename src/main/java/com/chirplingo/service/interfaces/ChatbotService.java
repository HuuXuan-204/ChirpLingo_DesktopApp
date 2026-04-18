package com.chirplingo.service.interfaces;

public interface ChatbotService {
    
    /**
     * Gửi tin nhắn đến chatbot
     * @param message Tin nhắn cần gửi
     * @return Phản hồi từ chatbot
     */
    public String sendMessage(String message);

    /**
     * Xóa lịch sử chat
     */
    public void clearHistory();

}
