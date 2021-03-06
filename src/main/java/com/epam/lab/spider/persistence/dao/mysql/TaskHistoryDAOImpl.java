package com.epam.lab.spider.persistence.dao.mysql;

import com.epam.lab.spider.persistence.dao.TaskHistoryDAO;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author Yura Kovalik
 */
public class TaskHistoryDAOImpl extends BaseDAO implements TaskHistoryDAO {

    private static final String SQL_GET_VK_INNER_POST_ID = "SELECT DISTINCT vk_inner_post_id FROM " +
            "task_synchronized_data " +
            "WHERE task_id = ? AND wall_id = ? ORDER BY processing_number DESC LIMIT ?;";

    private static final String SQL_DELETE_BY_TASK_ID = "DELETE FROM task_synchronized_data WHERE task_id = ?";
    private static final String SQL_DELETE_BY_Wall_ID = "DELETE FROM task_synchronized_data WHERE wall_id = ?";

    private static final String SQL_SET_VK_INNER_POST_ID = "INSERT INTO task_synchronized_data (task_id, wall_id, " +
            "vk_inner_post_id) VALUES (?, ?, ?)";
    private static final String SQL_CHECK_VK_INNER_POST_ID = "SELECT count(*)>0 FROM task_synchronized_data WHERE " +
            "vk_inner_post_id = 1 AND task_id = 1 AND wall_id = 1";
    private static final String SQL_STATISTICS_EXECUTION_QUERY = "SELECT COUNT(*) AS count, DATE_FORMAT" +
            "(task_synchonized_data, '%Y-%m-%d %H') AS date FROM task_synchronized_data WHERE task_synchonized_data " +
            "> ? AND task_synchonized_data <= ? GROUP BY UNIX_TIMESTAMP (task_synchonized_data) DIV 3600;";



    @Override
    public boolean deleteByTaskId(Connection connection, int taskId) throws SQLException {
        return changeQuery(connection, SQL_DELETE_BY_TASK_ID, taskId);
    }

    @Override
    public boolean deleteByWallId(Connection connection, int wallId) throws SQLException {
        return changeQuery(connection, SQL_DELETE_BY_Wall_ID, wallId);
    }


    @Override
    public Integer getIdLastProcessedPost(Connection connection, Integer taskId, Integer wallId) throws SQLException {
        ResultSet rs = selectQuery(connection, SQL_GET_VK_INNER_POST_ID, taskId, wallId, 1);
        if (rs.next()) {
            return rs.getInt(1);
        } else return null;
    }

    @Override
    public Set<Integer> getProcessedPost(Connection connection, Integer taskId, Integer wallId, int limit) throws
            SQLException {
        Set<Integer> set = new HashSet<>();
        ResultSet rs = selectQuery(connection, SQL_GET_VK_INNER_POST_ID, taskId, wallId, limit);
        while (rs.next()) {

            set.add(rs.getInt(1));
        }
        return set;
    }

    @Override
    public boolean checkIdProcessedPost(Connection connection, Integer vkInnerPostId, Integer taskId, Integer wallId)
            throws SQLException {
        ResultSet rs = selectQuery(connection, SQL_CHECK_VK_INNER_POST_ID, vkInnerPostId, taskId, wallId);
        if (rs.next()) {
            return rs.getBoolean(1);
        }
        return false;
    }

    @Override
    public boolean markIdLastProcessedPost(Connection connection, Integer vkInnerPostId, Integer taskId, Integer
            wallId) throws SQLException {
        return changeQuery(connection, SQL_SET_VK_INNER_POST_ID, taskId, wallId, vkInnerPostId);
    }

    @Override
    public boolean clearOldData(Connection connection) throws SQLException {
        return false;
    }

    @Override
    public Map<Long, Integer> statisticsExecution(Connection connection, String date) throws SQLException {
        String fromDate = date + " 00:00:00";
        String toDate = date + "23:59:59";
        ResultSet rs = selectQuery(connection, SQL_STATISTICS_EXECUTION_QUERY, fromDate, toDate);
        Map<Long, Integer> statistics = new HashMap<>();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        while (rs.next()) {
            String format = rs.getString("date") + ":00:00";
            try {
                statistics.put(dateFormat.parse(format).getTime(), rs.getInt("count"));
            } catch (ParseException e) {
                LOG.error(e.getLocalizedMessage(), e);
            }
        }
        return statistics;
    }
}
