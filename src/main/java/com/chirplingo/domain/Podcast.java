package com.chirplingo.domain;

import java.time.OffsetDateTime;

import com.chirplingo.domain.base.Fetchable;

import com.fasterxml.jackson.annotation.JsonProperty;

public class Podcast implements Fetchable {
    
    @JsonProperty("id")
    private String id;
    
    @JsonProperty("title")
    private String title;
    
    @JsonProperty("image_url")
    private String imageUrl;
    
    @JsonProperty("source_url")
    private String sourceUrl;
    
    @JsonProperty("published_at")
    private OffsetDateTime publishedAt;

    public Podcast() {
    }

    public Podcast(String id, String title, String imageUrl, String sourceUrl, OffsetDateTime publishedAt){
        this.id = id;
        this.title = title;
        this.imageUrl= imageUrl;
        this.sourceUrl = sourceUrl;
        this.publishedAt = publishedAt;
    }

    public String getId() {
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getImageUrl(){
        return this.imageUrl;
    }
    @Override
    public String getSourceUrl(){
        return this.sourceUrl;
    }

    public OffsetDateTime getPublishedAt() {
        return this.publishedAt;
    }
}
