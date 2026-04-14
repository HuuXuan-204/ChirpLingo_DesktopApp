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

public abstract class BaseRepository<T> implements Repository<T> {
    protected DatabaseManager dbManager;
    protected String tableName;

    protected BaseRepository (String tableName) {
        this.tableName = tableName;
        this.dbManager = DatabaseManager.getInstance();
    }

    @Override
    public void save(T entity) {
        if(entity == null) return;
        String sql = getUpsertSql();
        Object[] params = getUpsertParams(entity);
        executeUpdate(sql, params);
    }

    @Override
    public void saveMulti(List<T> entities) {
        if (entities == null || entities.isEmpty()) return;

        String sql = getUpsertSql();
        
        dbManager.executeWrite(() -> {
            try (Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try(PreparedStatement st = conn.prepareStatement(sql)) {
                    for (T entity : entities) {
                        Object[] params = getUpsertParams(entity);
                        bindParams(st, params);
                        st.addBatch();
                    }

                    st.executeBatch();
                    conn.commit();
                    System.out.println("SaveMulti thành công bảng " + tableName);
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi thực thi saveMulti cho bảng " + tableName, e);
            }
         });
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
        String sql = "SELECT * FROM " + tableName + " WHERE user_id = ? AND is_synced = 0";
        List<T> list = queryList(sql, userId);
        return list;
    }

    @Override
    public void softDelete(String id) {
        if (id == null) return;
        String sql = "UPDATE " + tableName + " SET deleted_at = ? WHERE user_id = ? AND id = ?";
        executeUpdate(sql, CommonUtils.getOffsetDateTime().toString(), getUserId(), id);
    }

    @Override
    public void softDeleteMulti (List<String> ids) {
        String userId = getUserId();
        if(ids == null || ids.isEmpty() || userId == null) return;
        String sql = "UPDATE " + tableName + " SET deleted_at = ? WHERE user_id = ? AND id = ?";
        
        dbManager.executeWrite(() -> {
            try(Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try(PreparedStatement st = conn.prepareStatement(sql)) {
                    for (String id : ids) {
                        st.setObject(1, CommonUtils.getOffsetDateTime().toString());
                        st.setObject(2, userId);
                        st.setObject(3, id);
                        st.addBatch();
                    }
                    st.executeBatch();
                    conn.commit();
                    System.out.println("SoftDeleteMulti thành công bảng " + tableName);
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi thực thi softDeleteMulti cho bảng " + tableName, e);
            }
        });
    }

    @Override
    public void hardDeleteMulti(List<String> ids) {
        String userId = getUserId();
        if(ids == null || ids.isEmpty() || userId == null) return;
        String sql = "DELETE FROM " + tableName + " WHERE user_id = ? AND id = ?";
        dbManager.executeWrite(() -> {
            try(Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try(PreparedStatement st = conn.prepareStatement(sql)) {
                    for(String id : ids) {
                        st.setObject(1, userId);
                        st.setObject(2, id);
                        st.addBatch();
                    }
                    st.executeBatch();
                    conn.commit();
                    System.out.println("HardDeleteMulti thành công bảng " + tableName);

                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi thực thi hardDeleteMulti cho bảng " + tableName);
            }
        });
    }

    @Override
    public void markSynced(List<String> ids) {
        String userId = getUserId();
        if(ids == null || ids.isEmpty() || userId == null) return;

        String sql = "UPDATE " + tableName + " SET is_synced = 1 WHERE id = ? AND user_id = ?";
        dbManager.executeWrite(() -> {
            try (Connection conn = dbManager.getConnection()) {
                conn.setAutoCommit(false);

                try(PreparedStatement st = conn.prepareStatement(sql)) {
                    for(String id : ids) {
                        st.setObject(1, id);
                        st.setObject(2, userId);
                        st.addBatch();
                    }
                    st.executeBatch();
                    conn.commit();
                    System.out.println("MarkSynced thành công bảng " + tableName);
                } catch (SQLException e) {
                    conn.rollback();
                    throw e;
                } finally {
                    conn.setAutoCommit(true);
                }

            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi khi thực thi markSynced cho bảng " + tableName);
            }
        });

    }


    /**
     * Thực thi câu lệnh UPDATE, INSERT, DELETE
     * @param sql SQL truy vấn
     * @param params Tham số truy vấn
     */
    protected void executeUpdate(String sql, Object... params) {
        dbManager.executeWrite(() -> {
            try (Connection conn = dbManager.getConnection();
                PreparedStatement st = conn.prepareStatement(sql)) {
                    bindParams(st, params);
                    st.executeUpdate();
                    System.out.println("Update thành công bảng " + tableName);
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Lỗi thực thi SQL: " + sql, e);
            }
        });
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
            throw new RuntimeException("Chưa đăng nhập bạn ơi!");
        }
        return id;
    }
}
