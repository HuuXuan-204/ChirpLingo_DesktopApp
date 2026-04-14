package com.chirplingo.domain.newspaper;

public class ContentSegment {
    private String en;
    private String vn;
    private int  orderIndex;

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
