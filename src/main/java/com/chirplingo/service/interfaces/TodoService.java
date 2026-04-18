package com.chirplingo.service.interfaces;

import java.util.List;
import com.chirplingo.domain.TodoItem;
import com.chirplingo.domain.Result;

public interface TodoService {
    /**
     * Lưu một việc cần làm
     * @param todo Việc cần làm cần lưu
     * @return true nếu lưu thành công, false nếu thất bại
     */
    public Result saveTodo(TodoItem todo);

    /**
     * Lấy tất cả việc cần làm
     * @return Danh sách tất cả việc cần làm
     */
    public List<TodoItem> getAllTodos();

    /**
     * Lấy danh sách việc cần làm trong ngày
     * @return Danh sách việc cần làm trong ngày
     */
    public List<TodoItem> getTodayTodos();

    /**
     * Lấy danh sách việc cần làm sắp tới hạn
     * @return Danh sách việc cần làm sắp tới hạn
     */
    public List<TodoItem> getUpcomingTodos();

    /**
     * Lấy danh sách việc cần làm quá hạn
     * @return Danh sách việc cần làm quá hạn
     */
    public List<TodoItem> getOverdueTodos();

    /**
     * Lấy danh sách việc cần làm đã hoàn thành
     * @return Danh sách việc cần làm đã hoàn thành
     */
    public List<TodoItem> getCompletedTodos();

    /**
     * Xóa mềm một việc cần làm
     * @param id ID việc cần làm cần xóa
     * @return true nếu xóa thành công, false nếu thất bại
     */
    public boolean softDeleteTodo(String id);

}
