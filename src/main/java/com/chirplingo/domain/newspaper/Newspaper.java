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

    public Newspaper(String id, String title, String imageURL, publishedAt OffsetDateTime, ArrayList<ContentSegment> segment){
        this.id = id;
        this.title = title;
        this.imageURL= imageURL;
        this.publishedAt = publishedAt;
        this.segments = segment;  
    }

    public String getId() {
        return id;
    }
    public String getTitle(){
        return title;
    }
    @Override
    public String SourceURL(){
        return imageURL;
    }
    public OffsetDateTime getPublic(){
        return publishedAt;
    }
    public ArrayList<ContentSegment> getSegment(){
        return segments;
    }
    public String getPreview(){
        
    }
}
