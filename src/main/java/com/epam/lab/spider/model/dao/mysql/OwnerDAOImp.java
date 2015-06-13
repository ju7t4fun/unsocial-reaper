package com.epam.lab.spider.model.dao.mysql;

import com.epam.lab.spider.model.dao.OwnerDAO;
import com.epam.lab.spider.model.entity.Owner;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Marian Voronovskyi on 12.06.2015.
 */
public class OwnerDAOImp extends BaseDAO implements OwnerDAO {
    private static final String SQL_INSERT_QUERY = "INSERT INTO owner (vk_id, name, domain) " +
            "VALUES (?, ?, ?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE owner SET vk_id = ?, name = ?, domain = ? " +
            "WHERE id = ?";
   // private static final String SQL_DELETE_QUERY = "DELETE * FROM owner WHERE id = ?";
    private static final String SQL_DELETE_QUERY = "UPDATE owner SET deleted = true WHERE id = ?";
    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM owner";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM owner WHERE id = ?";


    @Override
    public boolean insert(Connection connection, Owner owner) throws SQLException {
        return changeQuery(connection, SQL_INSERT_QUERY,
                owner.getVk_id(),
                owner.getName(),
                owner.getDomain());
    }

    @Override
    public boolean update(Connection connection, int id, Owner owner) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY,
                owner.getVk_id(),
                owner.getName(),
                owner.getDomain(),
                id);
    }

    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public List<Owner> select(Connection connection, String query, Object... args) throws SQLException {
        List<Owner> ownerList = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        Owner owner;
        while (rs.next()) {
            owner = new Owner();
            owner.setId(rs.getInt("id"));
            owner.setVk_id(rs.getInt("vk_id"));
            owner.setName(rs.getString("name"));
            owner.setDomain((rs.getString("domain")));
            ownerList.add(owner);
        }
        return ownerList;
    }

    @Override
    public List<Owner> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public Owner getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }
}
