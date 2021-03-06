package com.epam.lab.spider.controller.servlet;

import com.epam.lab.spider.controller.command.ActionFactory;
import com.epam.lab.spider.controller.command.owner.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Dzyuba Orest
 */
public class OwnerServlet extends HttpServlet {
    private static ActionFactory factory = new OwnerActionFactory();

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        factory.action(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {
        factory.action(request, response);
    }

    private static class OwnerActionFactory extends ActionFactory {

        public OwnerActionFactory() {
            commands = new HashMap<>();
            commands.put("default", new ShowBindAccountCommand());
            commands.put("get", new GetOwnerCommand());
            commands.put("remove", new RemoveOwnerCommand());
            commands.put("optionFilling", new OptionFillingCommand());
            commands.put("bind", new BindOwnerCommand());
            commands.put("stat", new GetGroupStatsCommand());
            commands.put("add", new AddNewOwnerCommand());
            commands.put("editowner", new EditOwnerNameCommand());
            commands.put("getOwnerWall", new GetOwnerWallCommand());
            commands.put("bindadmin", new BindAdminOwnerCommand());
        }
    }
}
