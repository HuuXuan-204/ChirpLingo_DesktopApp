package com.chirplingo.domain;

import java.time.LocalDate;

import com.chirplingo.domain.base.Fetchable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class WordOfDay implements Fetchable {
    @JsonProperty("id")
    private String id;

    @JsonProperty("word")
    private String word;

    @JsonProperty("meaning")
    private String meaning;

    @JsonProperty("type")
    private String type;

    @JsonProperty("example")
    private String example;

    @JsonProperty("source_url")
    private String sourceUrl;

    @JsonProperty("fetch_date")
    private LocalDate fetchDate;

    public WordOfDay() {
    }

    public WordOfDay(String id, String word, String meaning, String type, String example, String sourceUrl, LocalDate fetchDate){
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.type = type;
        this.example = example;
        this.sourceUrl = sourceUrl;
        this.fetchDate = fetchDate;
    }

    public String getId(){
        return this.id;
    }

    public String getWord(){
        return this.word;
    }

    public String getMeaning(){
        return this.meaning;
    }

    public String getType(){
        return this.type;
    }

    public String getExample(){
        return this.example;
    }

    public LocalDate getFetchDate(){
        return this.fetchDate;
    }

    @Override
    public String getSourceUrl(){
        return this.sourceUrl;
    }
    
    /**
     * Kiểm tra xem data này hết hạn chưa (Để quyết định xem có cần fetch lại không)
     * @return true nếu fetchDate không phải hôm nay và ngược lại
    */
    public boolean isExpired(){
        return !(LocalDate.now().equals(fetchDate));
    }
    
}
