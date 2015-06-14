package com.epam.lab.spider.model.service;

import com.epam.lab.spider.model.PoolConnection;
import com.epam.lab.spider.model.dao.DAOFactory;
import com.epam.lab.spider.model.dao.ProfileDAO;
import com.epam.lab.spider.model.dao.WallDao;
import com.epam.lab.spider.model.entity.Profile;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.List;

/**
 * Created by Boyarsky Vitaliy on 12.06.2015.
 */
public class ProfileService implements BaseService<Profile> {

    private DAOFactory factory = DAOFactory.getInstance();
    private ProfileDAO pdao = factory.create(ProfileDAO.class);
    private WallDao wdao = factory.create(WallDao.class);

    @Override
    public boolean insert(Profile profile) {
        try (Connection connection = PoolConnection.getConnection()) {
            return pdao.insert(connection, profile);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean update(int id, Profile profile) {
        try (Connection connection = PoolConnection.getConnection()) {
            return pdao.update(connection, id, profile);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    @Override
    public boolean delete(int id) {
        boolean res = false;
        try {
            Connection connection = PoolConnection.getConnection();
            try {
                connection.setAutoCommit(false);
                wdao.deleteByProfileId(connection, id);
                res = pdao.delete(connection, id);
                connection.commit();
            } catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                if (connection != null) {
                    connection.setAutoCommit(true);
                    connection.close();
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return res;
    }

    @Override
    public List<Profile> getAll() {
        try (Connection connection = PoolConnection.getConnection()) {
            return pdao.getAll(connection);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public Profile getById(int id) {
        try (Connection connection = PoolConnection.getConnection()) {
            return pdao.getById(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    public Profile getByVkId(int vkId) {
        try (Connection connection = PoolConnection.getConnection()) {
            return pdao.getByVkId(connection, vkId);
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }

    public List<Profile> getByUserId(int id) {
        try (Connection connection = PoolConnection.getConnection()) {
            return pdao.getByUserId(connection, id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }
}
