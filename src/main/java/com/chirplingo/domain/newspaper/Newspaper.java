package com.chirplingo.domain.newspaper;

import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.ArrayList;

import com.chirplingo.domain.base.Fetchable;

public class Newspaper implements Fetchable {
    private String id;
    private String title;
    private String imageURL;
    private OffsetDateTime publishedAt;
    private ArrayList<ContentSegment> segments;

    public Newspaper(String id, String title, String imageURL, OffsetDateTime publishedAt, ArrayList<ContentSegment> segments){
        this.id = id;
        this.title = title;
        this.imageURL= imageURL;
        this.publishedAt = publishedAt;
        this.segments = segments;  
    }

    public String getId() {
        return this.id;
    }

    public String getTitle(){
        return this.title;
    }

    @Override
    public String getSourceURL() {
        return this.imageURL;
    }

    public OffsetDateTime getPublishAt(){
        return this.publishedAt;
    }
    public ArrayList<ContentSegment> getSegments(){
        return this.segments;
    }
    /**public String getPreview(){
        
    }*/
}
