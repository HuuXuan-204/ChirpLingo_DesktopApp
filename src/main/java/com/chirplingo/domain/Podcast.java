package com.chirplingo.domain;

import com.chirplingo.domain.base.Fetchable;

public class Podcast implements Fetchable {
    private String id;
    private String title;
    private String imageURL;
    private String sourceURL;
    private String channelURL;


    public Podcast(String id, String title, String imageURL, String sourceURL, String channelURL){
        this.id = id;
        this.title = title;
        this.imageURL= imageURL;
        this.sourceURL = sourceURL;
        this.channelURL = channelURL;
    }

    public String getId() {
        return this.id;
    }
    public String getTitle(){
        return this.title;
    }
    public String getImageURL(){
        return this.imageURL;
    }
    @Override
    public String getSourceURL(){
        return this.sourceURL;
    }

    public String getChannelURL() {
        return this.channelURL;
    }
}
