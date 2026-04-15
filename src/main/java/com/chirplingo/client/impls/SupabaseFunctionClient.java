package com.chirplingo.client.impls;

import java.net.http.HttpRequest;

import com.chirplingo.client.base.BaseSupabaseClient;
import com.chirplingo.client.base.SupabaseContext;
import com.chirplingo.client.interfaces.RemoteFunctionClient;
import com.chirplingo.domain.ChatMessage;
import java.util.List;
import java.net.http.HttpResponse;
import java.util.Map;
import java.util.HashMap;
import java.util.ArrayList;
import java.util.Arrays;
import com.fasterxml.jackson.core.type.TypeReference;

public class SupabaseFunctionClient extends BaseSupabaseClient implements RemoteFunctionClient {
    
    public SupabaseFunctionClient(SupabaseContext context) {
        super(context);
    }

    @Override
    public Object call(String functionName, Object... params) {
        try {
            String endpoint = "/functions/v1/" + functionName;

            String payload = context.getMapper().writeValueAsString(params);

            HttpRequest request = createRequest(endpoint)
                .POST(HttpRequest.BodyPublishers.ofString(payload))
                .build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if(status == 200 || status == 201) {
                return response.body();
            } else {
                System.err.println("Lỗi Function [" + status + "]: " + response.body());
                return null;
            }
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    @Override
    public String callChatbot(String message, List<ChatMessage> history) {
        try {
            String endpoint = "/functions/v1/gemini-chat";

            List<Map<String, Object>> formattedHistory = transformHistory(history);

            Map<String, Object> body = new HashMap<>();
            message = message + "\n\n[HƯỚNG DẪN HỆ THỐNG (BẮT BUỘC): Sau khi trả lời xong, phải cập nhật bản tóm tắt nội dung hội thoại (không quá 150 từ) sao cho không bỏ sót bất kỳ thông tin nào từ đầu buổi chat, BẮT BUỘC luôn kết thúc phản hồi bằng cấu trúc: ||SUMMARY|| [Nội dung tóm tắt mới], KHÔNG ĐƯỢC QUÊN đoạn ||SUMMARY|| trong bất kỳ hoàn cảnh nào.";
            body.put("message", message);
            body.put("history", formattedHistory);

            String payload = context.getMapper().writeValueAsString(body);

            HttpRequest request = createRequest(endpoint)
                    .POST(HttpRequest.BodyPublishers.ofString(payload))
                    .build();

            HttpResponse<String> response = context.getHttpClient().send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status == 200 || status == 201) {
                Map<String, String> result = context.getMapper().readValue(
                    response.body(), 
                    new TypeReference<Map<String, String>>() {}
                );
                return result.get("text");
            } 
            else {
                System.err.println("Lỗi call Chatbot [" + status + "]: " + response.body());
                return getErrorMessage(status, response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
            return getErrorMessage(-1, e.getMessage());
        }
    }

    private List<Map<String, Object>> transformHistory(List<ChatMessage> history) {
        List<Map<String, Object>> formattedHistory = new ArrayList<>();
        if (history != null) {
            for (ChatMessage msg : history) {
                Map<String, Object> formattedMsg = new HashMap<>();
                formattedMsg.put("role", msg.getRole().equalsIgnoreCase("user") ? "user" : "model");
                
                Map<String, String> textPart = new HashMap<>();
                textPart.put("text", msg.getContent());
                formattedMsg.put("parts", Arrays.asList(textPart));

                formattedHistory.add(formattedMsg);
            }
        }
        return formattedHistory;
    }

    private String getErrorMessage(int status, String errorString) {
        if (errorString == null) errorString = "";
        
        if (status == 429) {
            return "Hết lượt miễn phí hôm nay rồi! Cú Mèo cần nghỉ ngơi xíu. Thử lại sau 1-5 phút nhé!";
        } else if (status == 503) {
            return "Server đang bận hoặc bảo trì. Bạn thử lại sau vài phút nha!";
        } else if (errorString.contains("429") || errorString.contains("Quota") || errorString.contains("Too Many Requests")) {
            return "Quá nhiều câu hỏi rồi, Cú Mèo chóng mặt. Nghỉ tí rồi hỏi tiếp nhé!";
        } else {
            String[] networkMsgs = {
                "Úi, Cú Mèo vấp phải cành cây rồi! Bạn nhắn lại câu vừa nãy giúp mình nhé?",
                "Mạng chập chờn quá, tín hiệu vũ trụ không gửi tới được Cú rồi",
                "Hình như Wifi nhà mình đi vắng. Bạn kiểm tra lại mạng xem sao?",
                "Cú Mèo đang đi lạc... Alo alo, bạn nói lại đi mình nghe chưa rõ?",
                "Có chút trục trặc nhỏ xíu. Thử lại lần nữa xem sao bạn ơi!"
            };
            return networkMsgs[(int)(Math.random() * networkMsgs.length)];
        }
    }
}
