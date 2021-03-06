package com.epam.lab.spider.controller.servlet.admin;

import com.epam.lab.spider.controller.command.ActionFactory;
import com.epam.lab.spider.controller.command.admin.stats.GetServiceStatisticCommand;
import com.epam.lab.spider.controller.command.admin.stats.GetVisitorsStatsCommand;
import com.epam.lab.spider.controller.command.admin.users.ShowAdminAllUsers;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Dzyuba Orest
 */
public class AdminServlet extends HttpServlet {

    private static ActionFactory factory = new AdminActionFactory();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        factory.action(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        factory.action(request, response);
    }

    private static class AdminActionFactory extends ActionFactory {

        public AdminActionFactory() {
            commands = new HashMap<>();
            commands.put("default", new ShowAdminAllUsers());
            commands.put("stats", new GetVisitorsStatsCommand());
            commands.put("statsService", new GetServiceStatisticCommand());
        }
    }
}
