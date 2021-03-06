package com.epam.lab.spider.persistence.dao.mysql;

import com.epam.lab.spider.model.SynchronizedWrapperUtils;
import com.epam.lab.spider.model.entity.Wall;
import com.epam.lab.spider.persistence.dao.WallDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author Dzyuba Orest
 */
public class WallDAOImp extends BaseDAO implements WallDAO {

    private static final String SQL_INSERT_QUERY = "INSERT INTO wall (owner_id, profile_id, permission, deleted) " +
            " VALUES (?, ?, ?, ?)";


    private static final String SQL_UPDATE_QUERY = "UPDATE wall SET owner_id = ?, profile_id = ?, permission = ?, " +
            "deleted = ? WHERE id = ?";
    //private static final String SQL_DELETE_QUERY = "DELETE FROM wall WHERE id = ?";
    private static final String SQL_DELETE_QUERY = "UPDATE wall SET deleted = 1 WHERE id = ?";
    private static final String SQL_DELETE_BY_OWNER_ID_QUERY = "UPDATE wall SET deleted = 1 WHERE owner_id = ?";
    private static final String SQL_DELETE_BY_OWNER_ID_AND_PERMISSION_QUERY = "UPDATE wall SET deleted = 1 WHERE " +
            "owner_id = ? AND profile_id=? AND permission=?";
    private static final String SQL_GET_ALL_QUERY = "SELECT * FROM wall WHERE deleted = 0";
    private static final String SQL_GET_BY_ID_QUERY = "SELECT * FROM wall WHERE id = ? AND deleted = 0";
    private static final String SQL_GET_BY_OWNER_ID_AND_PERMISSION_QUERY = "SELECT * FROM wall WHERE owner_id = ? AND " +
            "permission=? AND deleted = false";
    private static final String SQL_GET_DESTINATION_BY_TASK_ID_QUERY = "SELECT * FROM wall JOIN task_destination ON " +
            "task_destination.wall_id = wall.id WHERE task_id = ?";
    private static final String SQL_GET_SOURCE_BY_TASK_ID_QUERY = "SELECT * FROM wall JOIN task_source ON " +
            "task_source.wall_id = wall.id WHERE task_id = ?";
    private static final String SQL_DELETED_BY_PROFILE_ID_QUERY = "UPDATE wall SET deleted = 1 WHERE profile_id = ? " +
            "AND deleted = 0";
    private static final String SQL_GET_ALL_BY_PROFILE_ID_QUERY = "SELECT * FROM wall WHERE profile_id = ? AND " +
            "deleted = 0";

    private static final String SQL_CHECK_EXIST_QUERY = "SELECT * FROM wall WHERE profile_id = ? AND owner_id=? AND " +
            "permission=? AND deleted=true";
    private static final String SQL_UPDATE_ON_ACTIVE_QUERY = "UPDATE wall SET deleted = false WHERE owner_id = ? AND " +
            "profile_id = ? AND permission = ? AND deleted=true";
    private static final String SQL_GET_BY_USER_ID_QUERY = "SELECT wall.* FROM wall WHERE wall.profile_id IN (SELECT " +
            "profile.id FROM user JOIN profile ON user.id = profile.user_id  WHERE user.id = ?) AND deleted = 0";
    private static final String GET_READ_BY_USER_ID_QUERY = "SELECT wall.* FROM wall WHERE wall.profile_id IN (SELECT" +
            " profile.id FROM user JOIN profile ON user.id = profile.user_id  WHERE user.id = ?) AND permission = " +
            "'read' AND deleted = 0";
    private static final String GET_WRITE_BY_USER_ID_QUERY = "SELECT wall.* FROM wall WHERE wall.profile_id IN " +
            "(SELECT profile.id FROM user JOIN profile ON user.id = profile.user_id  WHERE user.id = ?) AND " +
            "permission = 'write' AND deleted = 0";
    private static final String SQL_GET_BY_OWNER_ID_QUERY = "SELECT * FROM wall WHERE owner_id = ? AND deleted = false";
    private static final String SQL_GET_WRITE_BY_OWNER_ID = "SELECT * FROM wall WHERE owner_id = ? AND deleted = false AND" +
            " permission = 'write'";
    private static final String SQL_GET_READ_BY_OWNER_ID = "SELECT * FROM wall WHERE owner_id = ? AND deleted = false AND" +
            " permission = 'read'";
    private static final String SQL_GET_WRITE_BY_ADMIN = "SELECT wall.* FROM wall JOIN profile ON profile.id = wall.profile_id JOIN user ON user.id = profile.user_id WHERE\n" +
            " role = 'ADMIN' AND user.deleted = 0 AND profile.deleted = 0 AND wall.deleted = 0 AND wall.permission = 'READ';";


    @Override
    public boolean insert(Connection connection, Wall wall) throws SQLException {
        Integer newId = insertQuery(connection, SQL_INSERT_QUERY,
                wall.getOwnerId(),
                wall.getProfileId(),
                wall.getPermission().toString().toUpperCase(),
                wall.getDeleted());
        setId(wall, newId);
        return newId != null;
    }

    @Override
    public boolean update(Connection connection, int id, Wall wall) throws SQLException {
        return changeQuery(connection, SQL_UPDATE_QUERY,
                wall.getOwnerId(),
                wall.getProfileId(),
                wall.getPermission(),
                wall.getDeleted(),
                id);
    }

    public boolean checkedExist(Connection connection, Wall wall) throws SQLException {
        return select(connection, SQL_CHECK_EXIST_QUERY, wall.getProfileId(), wall.getOwnerId(), wall.getPermission
                ().toString().toUpperCase()).size() > 0;
    }

    @Override
    public boolean delete(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETE_QUERY, id);
    }

    @Override
    public boolean deleteByOwnerId(Connection connection, int ownerDd) throws SQLException {
        return changeQuery(connection, SQL_DELETE_BY_OWNER_ID_QUERY, ownerDd);
    }

    @Override
    public boolean updateOnActive(Connection connection, int owner_id, int profile_id, Wall.Permission permission)
            throws SQLException {
        return changeQuery(connection, SQL_UPDATE_ON_ACTIVE_QUERY, owner_id, profile_id, permission.toString()
                .toUpperCase());
    }

    @Override
    public List<Wall> getByUserId(Connection connection, int userId) throws SQLException {
        return select(connection, SQL_GET_BY_USER_ID_QUERY, userId);
    }

    @Override
    public List<Wall> getReadByUserId(Connection connection, int userId) throws SQLException {
        return select(connection, GET_READ_BY_USER_ID_QUERY, userId);
    }

    @Override
    public List<Wall> getWriteByUserId(Connection connection, int userId) throws SQLException {
        return select(connection, GET_WRITE_BY_USER_ID_QUERY, userId);
    }

    @Override
    public List<Wall> getByOwnerId(Connection connection, int id) throws SQLException {
        return select(connection, SQL_GET_BY_OWNER_ID_QUERY, id);
    }

    @Override
    public List<Wall> getReadByOwnerId(Connection connection, int ownerId) throws SQLException {
        return select(connection, SQL_GET_READ_BY_OWNER_ID, ownerId);
    }

    @Override
    public List<Wall> getWriteByOwnerId(Connection connection, int ownerId) throws SQLException {
        return select(connection, SQL_GET_WRITE_BY_OWNER_ID, ownerId);
    }

    @Override
    public List<Wall> getWriteByAdmin(Connection connection) throws SQLException {
        return select(connection, SQL_GET_WRITE_BY_ADMIN);
    }

    @Override
    public boolean deleteByOwnerId(Connection connection, int owner_id, int profile_id, Wall.Permission permission)
            throws SQLException {
        return changeQuery(connection, SQL_DELETE_BY_OWNER_ID_AND_PERMISSION_QUERY, owner_id, profile_id, permission
                .toString().toUpperCase());
    }

    @Override
    public List<Wall> select(Connection connection, String query, Object... args) throws SQLException {
        List<Wall> walls = new ArrayList<>();
        ResultSet rs = selectQuery(connection, query, args);
        Wall wall;
        while (rs.next()) {
            wall = ENTITY_FACTORY.createWall();
            setId(wall, rs.getInt("id"));
            wall.setOwnerId(rs.getInt("owner_id"));
            wall.setProfileId(rs.getInt("profile_id"));
            wall.setPermission(Wall.Permission.valueOf(rs.getString("permission").toUpperCase()));
            wall.setDeleted(rs.getBoolean("deleted"));
            walls.add(SynchronizedWrapperUtils.wrap(wall));
        }
        return walls;
    }

    @Override
    public List<Wall> getAll(Connection connection) throws SQLException {
        return select(connection, SQL_GET_ALL_QUERY);
    }

    @Override
    public List<Wall> getAllByOwnerIdAndPermission(Connection connection, int owner_id, Wall.Permission permission)
            throws SQLException {
        return select(connection, SQL_GET_BY_OWNER_ID_AND_PERMISSION_QUERY, owner_id, permission.toString()
                .toUpperCase());
    }

    @Override
    public List<Wall> getAllByProfileID(Connection connection, int profile_id) throws SQLException {
        return select(connection, SQL_GET_ALL_BY_PROFILE_ID_QUERY, profile_id);
    }


    @Override
    public Wall getById(Connection connection, int id) throws SQLException {
        return first(select(connection, SQL_GET_BY_ID_QUERY, id));
    }

    @Override
    public List<Wall> getDestinationByTaskId(Connection connection, int id) throws SQLException {
        return select(connection, SQL_GET_DESTINATION_BY_TASK_ID_QUERY, id);
    }

    @Override
    public List<Wall> getSourceByTaskId(Connection connection, int id) throws SQLException {
        return select(connection, SQL_GET_SOURCE_BY_TASK_ID_QUERY, id);
    }

    @Override
    public boolean deleteByProfileId(Connection connection, int id) throws SQLException {
        return changeQuery(connection, SQL_DELETED_BY_PROFILE_ID_QUERY, id);
    }

    @Override
    public List<Wall> getByProfileId(Connection connection, int id) throws SQLException {
        return select(connection, SQL_GET_ALL_BY_PROFILE_ID_QUERY, id);
    }
}
