package com.chirplingo.domain;

import com.chirplingo.domain.base.Fetchable;
import java.time.LocalDate;

import com.fasterxml.jackson.annotation.JsonProperty;

public class QuoteOfDay implements Fetchable{
    @JsonProperty("id")
    private String id;

    @JsonProperty("content")
    private String content;

    @JsonProperty("author")
    private String author;

    @JsonProperty("source_url")
    private String sourceUrl;
    
    @JsonProperty("fetch_date")
    private LocalDate fetchDate;
    

    public QuoteOfDay() {
    }

    public QuoteOfDay(String id, String content, String author, String sourceUrl, LocalDate fetchDate){
        this.id = id;
        this.content = content;
        this.author = author;
        this.sourceUrl = sourceUrl;
        this.fetchDate = fetchDate;
    }

    public String getId(){
        return this.id;
    }

    public String getContent(){
        return this.content;
    }

    public String getAuthor(){
        return this.author;
    }

    public LocalDate getFetchDate(){
        return this.fetchDate;
    }

    @Override
    public String getSourceUrl(){
        return this.sourceUrl;
    }

    public boolean isExpired(){
        return !(LocalDate.now().equals(fetchDate));
    }
}
