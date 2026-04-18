package com.chirplingo.repository.interfaces;

import java.util.List;
public interface Repository<T> {
    /**----------------------------------------------------------------------
     * Lưu 1 entity
     * @param entity Entity cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean save(T entity);

    /**----------------------------------------------------------------------
     * Lưu nhiều entity
     * @param entities List các entity cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public boolean saveMulti(List<T> entities);

    /**----------------------------------------------------------------------
     * Lấy entity đầu tiên trong DB
     * @return Entity đầu tiên
     */
    public T getFirst();

    /**----------------------------------------------------------------------
     * Lấy tất cả entity
     * @return List các entity
     */
    public List<T> getAll();

    /**----------------------------------------------------------------------
     * Lấy các entity chưa được đồng bộ với Cloud
     * @return List các entity chưa được đồng bộ
     */
    public List<T> getUnsynced();

    /**----------------------------------------------------------------------
     * Xóa mềm entity (đánh dấu DeletedAt = timestamp hiện tại)
     * @param id ID của entity cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean softDelete(String id);

    /**----------------------------------------------------------------------
     * Xóa mềm nhiều entity
     * @param ids List ID của các entity cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean softDeleteMulti(List<String> ids);

    /**----------------------------------------------------------------------
     * Xóa cứng nhiều entity (Xóa hẳn khỏi local DB)
     * @param ids List ID của các entity cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean hardDeleteMulti(List<String> ids);

    /**----------------------------------------------------------------------
     * Lấy danh sách entity theo danh sách ID
     * @param ids Danh sách ID cần tìm
     * @return List các entity tìm được
     */
    public List<T> findByIds(List<String> ids);

    /**----------------------------------------------------------------------
     * Đánh dấu entity đã được đồng bộ với Cloud
     * @param ids List ID của các entity cần đánh dấu
     * @return true nếu đánh dấu thành công, false nếu thất bại
     */
    public boolean markSynced(List<String> ids);
}
