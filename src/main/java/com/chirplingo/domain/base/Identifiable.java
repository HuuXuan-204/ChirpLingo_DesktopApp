package com.chirplingo.domain.base;

/**
 * Interface định nghĩa các đối tượng được định danh duy nhất
 */
public interface Identifiable {
    /**
     * @return Chuỗi ID của đối tượng
     */
    public String getId();
}
