package com.chirplingo.domain.newspaper;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ContentSegment {
    @JsonProperty("en")
    private String en;
    
    @JsonProperty("vn")
    private String vn;
    
    @JsonProperty("order_index")
    private int  orderIndex;

    public ContentSegment() {
    }

    public ContentSegment(String en, String vn, int orderIndex) {
        this.en = en;
        this.vn = vn;
        this.orderIndex = orderIndex;
    }

    public String getEn(){
        return this.en;
    }

    public String getVn(){
        return this.vn;
    }
    
    public int getOrderIndex(){
        return this.orderIndex;
    }
}
