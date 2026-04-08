package com.chirplingo.domain;

import com.chirplingo.domain.base.Fetchable;
import java.time.LocalDate;

public class QuoteOfDay implements Fetchable{
    private String id;
    private String content;
    private String author;
    private String sourceURL;
    private LocalDate fetchDate;
    

    public QuoteOfDay(String id, String content, String author, String sourceURL, LocalDate fetchDate){
        this.id = id;
        this.content = content;
        this.author = author;
        this.sourceURL = sourceURL;
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
    public String getSourceURL(){
        return this.sourceURL;
    }

    public boolean isExpired(){
        return !(LocalDate.now().equals(fetchDate));
    }
}
