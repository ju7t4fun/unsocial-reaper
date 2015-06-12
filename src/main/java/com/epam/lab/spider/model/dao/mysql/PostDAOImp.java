package com.epam.lab.spider.model.dao.mysql;

import com.epam.lab.spider.model.dao.BaseDAO;
import com.epam.lab.spider.model.entity.Post;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Sasha on 12.06.2015.
 */
public class PostDAOImp extends BaseDAO implements PostDAO {

    private static final String SQL_INSERT_QUERY = "INSERT INTO post (message) VALUES (?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE post SET message = ?";
    private static final String SQL_DELETE_QUERY = "DELETE FROM post WHERE id = ?";
    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM post";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM post WHERE id = ?";

    @Override
    public boolean insert(Connection connection, Post post) throws SQLException {
        return changeQuery(connection, SQL_INSERT_QUERY, post.getMessage());
    }

    @Override
    public boolean update(Connection connection, int id, Post post) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY, post.getMessage(), id);
    }

    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public List<Post> select(Connection connection, String query, Object... args) throws SQLException {
        List<Post> posts = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        Post post;
        while (rs.next()) {
            post = new Post();
            post.setId(rs.getInt("id"));
            post.setMessage(rs.getString("message"));
            posts.add(post);
        }
        return posts;
    }

    @Override
    public List<Post> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public Post getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }
}