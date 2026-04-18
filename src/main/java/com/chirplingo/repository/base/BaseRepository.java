package com.chirplingo.repository.base;

import com.chirplingo.repository.interfaces.Repository;
import com.chirplingo.utils.CommonUtils;
import com.chirplingo.domain.UserSession;
import com.chirplingo.repository.db.DatabaseManager;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.ArrayList;
import java.sql.ResultSet;
import java.util.stream.Collectors;

public abstract class BaseRepository<T> implements Repository<T> {
    protected DatabaseManager dbManager;
    protected String tableName;

    protected BaseRepository (String tableName) {
        this.tableName = tableName;
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public boolean save(T entity) {
        if (entity == null) return false;
        String sql = getUpsertSql();
        Object[] params = getUpsertParams(entity);
        return executeUpdate(sql, params);
    }

    @Override
    public boolean saveMulti(List<T> entities) {
        if (entities == null || entities.isEmpty()) return false;

        String sql = getUpsertSql();

        Boolean result = dbManager.executeWriteWithResult(() -> {
            try (Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    for (T entity : entities) {
                        Object[] params = getUpsertParams(entity);
                        bindParams(st, params);
                        st.addBatch();
                    }

                    st.executeBatch();
                    conn.commit();
                    System.out.println("SaveMulti thành công bảng " + tableName);
                    return true;
                } catch (SQLException e) {
                    conn.rollback();
                    e.printStackTrace();
                    return false;
                } finally {
                    conn.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });

        return result != null && result;
    }

    @Override
    public List<T> getAll() {
        String sql = getSelectSql();
        List<T> list = queryList(sql, getUserId());
        return list;
    }

    @Override
    public T getFirst() {
        String sql = getSelectSql();
        T entity = queryFirst(sql, getUserId());
        return entity;
    }

    @Override
    public List<T> getUnsynced() {
        String userId = getUserId();
        if (userId == null) return new ArrayList<>();
        String sql = "SELECT * FROM " + tableName + " WHERE " + getUserIdColumn() + " = ? AND is_synced = 0";
        List<T> list = queryList(sql, userId);
        return list;
    }

    @Override
    public boolean softDelete(String id) {
        if (id == null) return false;
        String sql = "UPDATE " + tableName + " SET deleted_at = ? WHERE " + getUserIdColumn() + " = ? AND id = ?";
        return executeUpdate(sql, CommonUtils.getOffsetDateTime().toString(), getUserId(), id);
    }

    @Override
    public boolean softDeleteMulti(List<String> ids) {
        String userId = getUserId();
        if (ids == null || ids.isEmpty() || userId == null) return false;
        String sql = "UPDATE " + tableName + " SET deleted_at = ? WHERE " + getUserIdColumn() + " = ? AND id = ?";
        String now = CommonUtils.getOffsetDateTime().toString();

        Boolean result = dbManager.executeWriteWithResult(() -> {
            try (Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);


                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    for (String id : ids) {
                        st.setObject(1, now);
                        st.setObject(2, userId);
                        st.setObject(3, id);
                        st.addBatch();
                    }
                    st.executeBatch();
                    conn.commit();
                    System.out.println("SoftDeleteMulti thành công bảng " + tableName);
                    return true;
                } catch (SQLException e) {
                    conn.rollback();
                    e.printStackTrace();
                    return false;
                } finally {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });

        return result != null && result;
    }

    @Override
    public boolean hardDeleteMulti(List<String> ids) {
        String userId = getUserId();
        if (ids == null || ids.isEmpty() || userId == null) return false;
        String sql = "DELETE FROM " + tableName + " WHERE " + getUserIdColumn() + " = ? AND id = ?";

        Boolean result = dbManager.executeWriteWithResult(() -> {
            try (Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    for (String id : ids) {
                        st.setObject(1, userId);
                        st.setObject(2, id);
                        st.addBatch();
                    }
                    st.executeBatch();
                    conn.commit();
                    System.out.println("HardDeleteMulti thành công bảng " + tableName);
                    return true;
                } catch (SQLException e) {
                    conn.rollback();
                    e.printStackTrace();
                    return false;
                } finally {
                    conn.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });

        return result != null && result;
    }

    @Override
    public List<T> findByIds(List<String> ids) {
        String userId = getUserId();
        if (ids == null || ids.isEmpty() || userId == null) return new ArrayList<>();

        String placeholders = ids.stream().map(id -> "?").collect(Collectors.joining(", "));
        String sql = "SELECT * FROM " + tableName
                + " WHERE " + getUserIdColumn() + " = ?"
                + " AND id IN (" + placeholders + ")";

        Object[] params = new Object[ids.size() + 1];
        params[0] = userId;
        for (int i = 0; i < ids.size(); i++) {
            params[i + 1] = ids.get(i);
        }

        return queryList(sql, params);
    }

    @Override
    public boolean markSynced(List<String> ids) {
        String userId = getUserId();
        if (ids == null || ids.isEmpty() || userId == null) return false;

        String sql = "UPDATE " + tableName + " SET is_synced = 1 WHERE id = ? AND " + getUserIdColumn() + " = ?";

        Boolean result = dbManager.executeWriteWithResult(() -> {
            try (Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try (PreparedStatement st = conn.prepareStatement(sql)) {
                    for (String id : ids) {
                        st.setObject(1, id);
                        st.setObject(2, userId);
                        st.addBatch();
                    }
                    st.executeBatch();
                    conn.commit();
                    System.out.println("MarkSynced thành công bảng " + tableName);
                    return true;
                } catch (SQLException e) {
                    conn.rollback();
                    e.printStackTrace();
                    return false;
                } finally {
                    conn.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });

        return result != null && result;
    }


    /**
     * Thực thi câu lệnh UPDATE, INSERT, DELETE
     * @param sql SQL truy vấn
     * @param params Tham số truy vấn
     * @return true nếu thành công, false nếu thất bại
     */
    protected boolean executeUpdate(String sql, Object... params) {
        Boolean result = dbManager.executeWriteWithResult(() -> {
            try (Connection conn = dbManager.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)) {
                    bindParams(st, params);
                    int rows = st.executeUpdate();
                    System.out.println("Update thành công bảng " + tableName);
                    return rows > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        });
        return result != null && result;
    }

    /**
     * Truy vấn nhiều entity, dùng cho bảng vocabulary, todo_list
     * @param sql SQL truy vấn
     * @param params Tham số truy vấn
     * @return List các entity T
     */
    protected List<T> queryList(String sql, Object... params) {
        List<T> list = new ArrayList<>();
        try(Connection conn = dbManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql)) {
                bindParams(st, params);
                ResultSet rs = st.executeQuery();
                while(rs.next()){
                    list.add(mapRow(rs));
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn SQL: " + sql, e);
        }
        return list;
    }

    /**
     * Truy vấn 1 entity, dùng cho bảng profiles, learn_history
     * @param sql SQL truy vấn
     * @param params Tham số truy vấn
     * @return Entity T
     */
    protected T queryFirst(String sql, Object... params) {
        try(Connection conn = dbManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql)) {
                bindParams(st, params);
                ResultSet rs = st.executeQuery();
                if(rs.next()) {
                    return mapRow(rs);
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn SQL: " + sql, e);
        }
        return null;
    }

    /**
     * Truy vấn số lượng, dùng cho bảng vocabulary, todo_list
     * @param sql SQL truy vấn
     * @param params Tham số truy vấn
     * @return Số lượng
     */
    protected int queryCount(String sql, Object... params) {
        try(Connection conn = dbManager.getConnection();
            PreparedStatement st = conn.prepareStatement(sql)) {
                bindParams(st, params);
                ResultSet rs = st.executeQuery();
                if(rs.next()) {
                    return rs.getInt(1);
                }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Lỗi truy vấn SQL: " + sql, e);
        }
        return 0;
    }

    /**
     * Map data truy vấn từ SQlite sang Entity
     * @param rs ResultSet chứa dữ liệu
     * @return Entity 
     * @throws SQLException
     */
    protected abstract T mapRow(ResultSet rs) throws SQLException;

    /**
     * Gán tham số vào PreparedStatement
     * @param st PreparedStatement cần gán tham số
     * @param params Mảng các tham số
     * @throws SQLException
     */
    protected void bindParams(PreparedStatement st, Object... params) throws SQLException {
        for (int i = 0; i < params.length; i++) {
            st.setObject(i + 1, params[i]);
        }
    }  

    protected abstract String getUpsertSql();
    protected abstract String getSelectSql();
    protected abstract Object[] getUpsertParams(T entity);
    
    protected String getUserId() {
        String id = UserSession.getInstance().getUserId();
        if (id == null) {
            System.err.println("Cảnh báo: Truy cập Repository [" + tableName + "] khi chưa đăng nhập.");
            return null;
        }
        return id;
    }

    protected String getUserIdColumn() {
        return "user_id";
    }
}
