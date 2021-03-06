package com.epam.lab.spider.persistence.dao.mysql;

import com.epam.lab.spider.model.SynchronizedWrapperUtils;
import com.epam.lab.spider.model.entity.Profile;
import com.epam.lab.spider.persistence.dao.ProfileDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Boyarsky Vitaliy
 */
public class ProfileDAOImp extends BaseDAO implements ProfileDAO {

    private static final String SQL_INSERT_QUERY = "INSERT INTO profile (user_id, vk_id, access_token, ext_time, " +
            "app_id, deleted, name) VALUES (?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE_QUERY = "UPDATE profile SET user_id = ?, vk_id = ?, access_token = ?, " +
            "ext_time = ?, app_id = ?, deleted = ?, name = ? WHERE id = ?";
    // private static final String SQL_DELETE_QUERY = "DELETE FROM vk_profile WHERE id = ?";
    private static final String SQL_DELETE_QUERY = "UPDATE profile SET deleted = true WHERE id = ?";
    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM profile WHERE deleted = false";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM profile WHERE id = ? AND deleted = false";
    private static final String SQL_GET_BY_VK_ID_QUERY = "SELECT * FROM profile WHERE vk_id = ? AND deleted = false";
    private static final String SQL_GET_BY_USER_ID_QUERY = "SELECT * FROM profile WHERE user_id = ? AND deleted = " +
            "false";
    private static final String SQL_GET_ALL_NOT_IN_WALL_QUERY =
            "SELECT * FROM profile WHERE profile.id NOT IN (SELECT profile_id FROM " +
                    " (SELECT wall.profile_id AS profile_id, owner.vk_id FROM" +
                    " (wall JOIN owner " +
                    "ON wall.owner_id=owner.id AND wall.deleted=false )" +
                    " WHERE owner.vk_id=? ) AS T)";
    private static final String SQL_GET_ALL_IN_WALL_QUERY =
            "SELECT * FROM profile WHERE profile.id IN (SELECT profile_id FROM " +
                    " (SELECT wall.profile_id AS profile_id, owner.vk_id FROM" +
                    " (wall JOIN owner " +
                    "ON wall.owner_id=owner.id AND wall.deleted=false )" +
                    " WHERE owner.vk_id=? ) AS T)";
    private static final String SQL_GET_BY_USER_ID_LIMIT_QUERY = "SELECT * FROM profile WHERE deleted = 0 AND user_id" +
            " = ? LIMIT ?, ?";
    private static final String SQL_GET_COUNT_BY_USER_ID = "SELECT COUNT(*) FROM profile WHERE user_id = ? AND " +
            "deleted = 0";

    @Override
    public boolean insert(Connection connection, Profile profile) throws SQLException {
        Integer newId = insertQuery(connection, SQL_INSERT_QUERY,
                profile.getUserId(),
                profile.getVkId(),
                profile.getAccessToken(),
                profile.getExtTime(),
                profile.getAppId(),
                profile.getDeleted(),
                profile.getName());
        setId(profile, newId);
        return newId != null;
    }

    @Override
    public boolean update(Connection connection, int id, Profile profile) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY,
                profile.getUserId(),
                profile.getVkId(),
                profile.getAccessToken(),
                profile.getExtTime(),
                profile.getAppId(),
                profile.getDeleted(),
                profile.getName(),
                id);
    }

    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public List<Profile> select(Connection connection, String query, Object... args) throws SQLException {
        List<Profile> profiles = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        Profile profile;
        while (rs.next()) {
            profile = ENTITY_FACTORY.createProfile();
            setId(profile, rs.getInt("id"));
            profile.setUserId(rs.getInt("user_id"));
            profile.setVkId(rs.getInt("vk_id"));
            profile.setAccessToken(rs.getString("access_token"));
            profile.setExtTime(rs.getTimestamp("ext_time"));
            profile.setAppId(rs.getInt("app_id"));
            profile.setDeleted(rs.getBoolean("deleted"));
            profile.setName(rs.getString("name"));
            profiles.add(SynchronizedWrapperUtils.wrap(profile));
        }
        return profiles;
    }

    @Override
    public List<Profile> getNotInWall(Connection connection, int owner_id) throws SQLException {
        return select(connection, SQL_GET_ALL_NOT_IN_WALL_QUERY, owner_id);
    }

    @Override
    public List<Profile> getInWall(Connection connection, int owner_id) throws SQLException {
        return select(connection, SQL_GET_ALL_IN_WALL_QUERY, owner_id);
    }

    @Override
    public List<Profile> getByUserId(Connection connection, Integer id, int page, int size) throws SQLException {
        return select(connection, SQL_GET_BY_USER_ID_LIMIT_QUERY, id, page, size);
    }

    @Override
    public int getCountByUserId(Connection connection, Integer id) throws SQLException {
        ResultSet rs = selectQuery(connection, SQL_GET_COUNT_BY_USER_ID, id);
        if (rs.next()) {
            return rs.getInt("COUNT(*)");
        }
        return 0;
    }

    @Override
    public List<Profile> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public Profile getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }

    @Override
    public List<Profile> getByUserId(Connection connection, int id) throws SQLException {
        return select(connection, SQL_GET_BY_USER_ID_QUERY, id);
    }

    @Override
    public Profile getByVkId(Connection connection, int vkId) throws SQLException {
        return first(select(connection, SQL_GET_BY_VK_ID_QUERY, vkId));
    }

}
