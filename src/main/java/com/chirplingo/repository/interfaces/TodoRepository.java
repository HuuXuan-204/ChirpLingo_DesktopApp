package com.chirplingo.repository.interfaces;

import com.chirplingo.domain.TodoItem;
import java.util.List;

public interface TodoRepository extends Repository<TodoItem> {
    
    /**---------------------------------------------------------------
     * Lấy danh sách công việc có deadline trong ngày hôm nay
     * @return List các công việc trong ngày
     */
    public List<TodoItem> getToday();

    /**---------------------------------------------------------------
     * Lấy danh sách công việc sắp tới phải hoàn thành
     * @return List các công việc sắp tới
     */
    public List<TodoItem> getUpcoming();

    /**---------------------------------------------------------------
     * Lấy danh sách công việc đã quá hạn
     * @return List các công việc đã quá hạn
     */
    public List<TodoItem> getOverDue();

    /**---------------------------------------------------------------
     * Lấy danh sách công việc đã hoàn thành
     * @return List các công việc đã hoàn thành
     */
    public List<TodoItem> getCompleted();

}
