package com.chirplingo.domain;

import com.chirplingo.domain.base.Fetchable;

public class Podcast implements Fetchable {
    private String id;
    private String title;
    private String imageURL;
    private String sourceURL;
    private String channelURL;


    public Podcast(String id, String title, String image, String sourceURL, String channelURL){
        this.id = id;
        this.title = title;
        this.imageURL= imageURL;
        this.sourceURL = sourceURL;
        this.channelURL = channelURL;
    }

    public String getId() {
        return id;
    }
    public String getTitle(){
        return title;
    }
    public String getImageURL(){
        return imageURL;
    }
    @Override
    public String getSourceURL(){
        return sourceURL;
    }
}
