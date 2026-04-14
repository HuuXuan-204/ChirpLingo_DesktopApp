package com.chirplingo.client.interfaces;

public interface RemoteStorageClient {
    /**
     * Upload data lên Storage chung
     * @param bucket Tên bucket
     * @param path Đường dẫn
     * @param data Dữ liệu
     * @return Đường dẫn công khai
     */
    public String upload(String bucket, String path, byte[] data);

    /**
     * Lấy đường dẫn công khai của data trên Storage
     * @param bucket Tên bucket
     * @param path Đường dẫn
     * @return Đường dẫn công khai
     */
    public String getPublicUrl(String bucket, String path);

    /**
     * Xóa file trên Storage chung
     * @param bucket Tên bucket
     * @param path Đường dẫn
     * @return true nếu xóa thành công và ngược lại
     */
    public boolean delete(String bucket, String path);
}
