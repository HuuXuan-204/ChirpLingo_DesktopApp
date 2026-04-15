package com.chirplingo.client.external;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.chirplingo.domain.WordOfDay;
import com.chirplingo.domain.QuoteOfDay;
import com.fasterxml.jackson.core.type.TypeReference;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import com.chirplingo.utils.CommonUtils;
import com.chirplingo.utils.Config;

public class ExternalApiClient {
    private final HttpClient client;
    private final ObjectMapper mapper;

    public ExternalApiClient(HttpClient client) {
        this.client = client;
        this.mapper = new ObjectMapper();
        this.mapper.registerModule(new JavaTimeModule());
        this.mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * Fetch từ vựng của ngày từ Wordnik API
     * @return WordOfDay
     */
    public WordOfDay fetchTodayWord() {
        String apiKey = Config.get("WORDNIK_API_KEY");
        if (apiKey == null) {
            System.err.println("Lỗi: WORDNIK_API_KEY chưa được cấu hình");
            return null;
        }

        String url = "https://api.wordnik.com/v4/words.json/wordOfTheDay?api_key=" + apiKey;

        try {
            HttpRequest request = createRequest(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if (status == 200 || status == 201) {
                WordNik data = mapper.readValue(response.body(), WordNik.class);

                if (data == null) return null;

                String word = data.word;
                String meaning = "";
                String type = "";

                if (data.definitions != null && !data.definitions.isEmpty()) {
                    WordNik.Definition first = data.definitions.get(0);
                    meaning = first.text;
                    type = first.partOfSpeech;
                }

                String exampleText = "";
                if (data.examples != null && !data.examples.isEmpty()) {
                    WordNik.Example first = data.examples.get(0);
                    exampleText = first.text;
                }

                return new WordOfDay(
                    CommonUtils.generateUUID(),
                    word,
                    meaning,
                    type,
                    exampleText,
                    "https://api.wordnik.com/v4/words.json/wordOfTheDay",
                    LocalDate.now()
                );
            } else {
                System.err.println("Lỗi FetchTodayWord [" + status + "]: " + response.body());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Fetch quote của ngày từ ZenQuotes API
     * @return QuoteOfDay
     */
    public QuoteOfDay fetchTodayQuote() {
        try {
            String url = "https://zenquotes.io/api/today";

            HttpRequest request = createRequest(url).GET().build();

            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

            int status = response.statusCode();

            if(status == 200 || status == 201){
                List<Map<String, Object>> quotes = mapper.readValue(response.body(), new TypeReference<List<Map<String, Object>>>(){});

                if(quotes != null && !quotes.isEmpty()){
                    Map<String, Object> quote = quotes.get(0);
                    return new QuoteOfDay(
                        CommonUtils.generateUUID(),
                        quote.get("q") != null ? quote.get("q").toString() : "",
                        quote.get("a") != null ? quote.get("a").toString() : "",
                        url,
                        LocalDate.now()
                    );
                }
            } else {
                System.err.println("Lỗi FetchTodayQuote [" + status + "]: " + response.body());
            }
            
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null; 
    }

    private HttpRequest.Builder createRequest(String url) {
        return HttpRequest.newBuilder()
                .uri(URI.create(url))
                .timeout(Duration.ofSeconds(10))
                .header("Accept", "application/json");
    }

    /**
     * DTO để lấy những trường cần thiết được fetch từ Wordnik API
     */
    private static class WordNik {
        public String word;
        public List<Definition> definitions;
        public List<Example> examples;

        static class Definition {
            public String text;
            public String partOfSpeech;
        }

        static class Example {
            public String text;
        }
    }
}
