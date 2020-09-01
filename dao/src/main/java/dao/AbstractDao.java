package dao;

import exception.BusinessException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

public abstract class AbstractDao<T> implements GenericDao<T> {

    protected final Logger logger = LogManager.getLogger(this.getClass().getSimpleName());

    @Override
    public void add(final T object, final Connection connection) {
        String sql = getAddQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            List<Object> values = getInsertData(object);
            for (int index = 1; index <= values.size(); index++) {
                statement.setObject(index, values.get(index - 1));
            }
            statement.execute();
        } catch (Exception ex) {
            logger.error("Error at adding object: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    @Override
    public List<T> getAll(final Connection connection) {
        List<T> list;
        String sql = getSelectAllQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            ResultSet rs = statement.executeQuery();
            list = parseSelectResultSet(rs);
        } catch (Exception ex) {
            logger.error("Error at getting all: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return list;
    }

    @Override
    public T getById(final Integer id, final Connection connection) {
        List<T> list;
        String sql = getSelectByIdQuery();
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, id);
            ResultSet rs = statement.executeQuery();
            list = parseSelectResultSet(rs);
        } catch (Exception ex) {
            logger.error("Error at getting by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
        return list.size() == 0 ? null : list.get(0);
    }

    @Override
    public void updateById(final Integer id, final Object value, final String columnName,
                           final Connection connection) {
        String sql = getUpdateByIdQuery(columnName);
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setObject(1, value);
            statement.setInt(2, id);
            statement.execute();
        } catch (Exception ex) {
            logger.error("Error at updating by id: " + ex.getMessage());
            throw new BusinessException(ex.getMessage());
        }
    }

    protected abstract String getTableName();

    protected abstract String getFields();

    protected abstract String getInsertJokers();

    protected abstract List<T> parseSelectResultSet(ResultSet rs);

    protected abstract List<Object> getInsertData(T object);

    protected String getSelectAllQuery() {
        return "select * from " + getTableName();
    }

    protected String getSelectByIdQuery() {
        return "select * from " + getTableName() + " where id = ?";
    }

    protected String getUpdateQuery(final String columnName) {
        return "update " + getTableName() + " set " + columnName + " = ?";
    }

    protected String getUpdateByIdQuery(final String columnName) {
        return getUpdateQuery(columnName) + " where id = ?";
    }

    protected String getAddQuery() {
        return "insert into " + getTableName() + " " + getFields() + " values" + getInsertJokers();
    }
}

