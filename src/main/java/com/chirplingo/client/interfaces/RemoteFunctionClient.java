package com.chirplingo.client.interfaces;

import com.chirplingo.domain.ChatMessage;
import java.util.List;

public interface RemoteFunctionClient {
    /**
     * Gọi function trên server
     * @param functionName Tên function
     * @param params Tham số
     * @return Kết quả
     */
    public Object call(String functionName, Object... params);

    /**
     * Gọi chatbot xử lý tin nhắn
     * @param message Tin nhắn
     * @param history Lịch sử trò chuyện
     * @return Kết quả
     */
    public String callChatbot (String message, List<ChatMessage> history);
}
