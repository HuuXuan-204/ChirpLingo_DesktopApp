package com.chirplingo.domain.newspaper;

import java.time.OffsetDateTime;
import java.util.Comparator;
import java.util.List;
import com.chirplingo.domain.base.Fetchable;

public class Newspaper implements Fetchable {
    private String id;
    private String title;
    private String imageURL;
    private OffsetDateTime publishedAt;
    private List<ContentSegment> segments;

    public Newspaper(String id, String title, String imageURL, OffsetDateTime publishedAt, List<ContentSegment> segments){
        this.id = id;
        this.title = title;
        this.imageURL= imageURL;
        this.publishedAt = publishedAt;
        if (segments != null) {
            segments.sort(Comparator.comparingInt(ContentSegment::getOrderIndex));
        }
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

    public List<ContentSegment> getSegments(){
        return this.segments;
    }

    /**
     * @return Đoạn nội dung demo của bài báo
     */
    public String getPreview(){
        if (segments == null || segments.isEmpty()) {
            return "";
        }

        String content = segments.get(0).getEn();
        if (content == null) return "";

        return content.length() <= 100 ? content : content.substring(0, 100) + "...";
    }
}
