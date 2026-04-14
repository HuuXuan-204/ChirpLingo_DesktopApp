package com.chirplingo.domain;

import java.time.LocalDate;

import com.chirplingo.domain.base.Fetchable;;

public class WordOfDay implements Fetchable {
    private String id;
    private String word;
    private String meaning;
    private String type;
    private String example;
    private String sourceURL;
    private LocalDate fetchDate;

    public WordOfDay(String id, String word, String meaning, String type, String example, String sourceURl, LocalDate fetchDate){
        this.id = id;
        this.word = word;
        this.meaning = meaning;
        this.type = type;
        this.example = example;
        this.sourceURL = sourceURl;
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
    public String getSourceURL(){
        return this.sourceURL;
    }
    
    /**
     * Kiểm tra xem data này hết hạn chưa (Để quyết định xem có cần fetch lại không)
     * @return true nếu fetchDate không phải hôm nay và ngược lại
    */
    public boolean isExpired(){
        return !(LocalDate.now().equals(fetchDate));
    }
    
}
