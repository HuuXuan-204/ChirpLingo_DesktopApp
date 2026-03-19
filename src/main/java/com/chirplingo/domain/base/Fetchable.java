package com.chirplingo.domain.base;

/**
 * Interface này định nghĩa các đối tượng chỉ kéo dữ liệu từ server hoặc nguồn khác về, không đẩy lên
 */
public interface Fetchable extends Identifiable{
    /**
     * Lấy link của nguồn dữ liệu gốc
     * @return Chuỗi URL trỏ đến nguồn (link ảnh bài báo; link ảnh, video, kênh Podcast,...)
     */
    public String getSourceURL();
}
