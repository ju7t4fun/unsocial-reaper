package com.epam.lab.spider.persistence.dao.mysql;

import com.epam.lab.spider.model.SynchronizedWrapperUtils;
import com.epam.lab.spider.model.entity.Filter;
import com.epam.lab.spider.persistence.dao.FilterDAO;
import com.epam.lab.spider.persistence.dao.savable.SavableDAOUtils;
import com.epam.lab.spider.persistence.dao.savable.exception.InvalidEntityException;
import com.epam.lab.spider.persistence.dao.savable.exception.ResolvableDAOException;
import com.epam.lab.spider.persistence.dao.savable.exception.UnsupportedDAOException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Marian Voronovskyi
 */
public class FilterDAOImp extends BaseDAO implements FilterDAO {

    private static final String SQL_INSERT_QUERY = "INSERT INTO filter (likes, reposts, comments, min_time, max_time," +
            " deleted) VALUES (?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE filter SET likes = ?, reposts = ?, comments = ?, min_time " +
            "= ?, max_time = ?, deleted = ? WHERE id = ?";
    //private static final String SQL_DELETE_QUERY = "DELETE * FROM filter WHERE id = ?";
    private static final String SQL_DELETE_QUERY = "UPDATE filter SET deleted = true WHERE id = ?";
    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM filter WHERE deleted = false";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM filter WHERE id = ? AND deleted = false";

    @Override
    public boolean insert(Connection connection, Filter filter) throws SQLException {
        Integer newId = insertQuery(connection, SQL_INSERT_QUERY,
                filter.getLikes(),
                filter.getRePosts(),
                filter.getComments(),
                filter.getMinTime(),
                filter.getMaxTime(),
                filter.getDeleted());
        setId(filter, newId);
        return newId != null;
    }

    @Override
    public boolean update(Connection connection, int id, Filter filter) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY,
                filter.getLikes(),
                filter.getRePosts(),
                filter.getComments(),
                filter.getMinTime(),
                filter.getMaxTime(),
                filter.getDeleted(),
                id);
    }

    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public List<Filter> select(Connection connection, String query, Object... args) throws SQLException {
        List<Filter> filterList = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        Filter filter;
        while (rs.next()) {
            filter = ENTITY_FACTORY.createFilter();
            setId(filter, rs.getInt("id"));
            filter.setLikes(rs.getInt("likes"));
            filter.setRePosts(rs.getInt("reposts"));
            filter.setComments(rs.getInt("comments"));
            filter.setMinTime(rs.getLong("min_time"));
            filter.setMaxTime(rs.getLong("max_time"));
            filter.setDeleted(rs.getBoolean("deleted"));
            filterList.add(SynchronizedWrapperUtils.wrap(filter));
        }
        return filterList;
    }

    @Override
    public List<Filter> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public Filter getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }

    @Override
    public boolean save(Connection conn, Filter entity) throws UnsupportedDAOException, ResolvableDAOException, InvalidEntityException {
        return SavableDAOUtils.save(conn, entity, this);
    }
}
