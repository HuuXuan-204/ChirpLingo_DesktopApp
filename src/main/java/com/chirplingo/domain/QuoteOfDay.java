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
    public String getID(){
        return id;
    }
    public String getContent(){
        return content;
    }
    public String getAuthor(){
        return author;
    }
    public LocalDate getFetchDate(){
        return fetchDate;
    }
    @Override
    public String getSourceURL(){
        return sourceURL;
    }
    public boolean isExpired(){
        return !LocalDate.now().equal(fetchDate);
    }
}
