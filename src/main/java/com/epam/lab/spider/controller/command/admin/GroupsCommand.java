package com.epam.lab.spider.controller.command.admin;

import com.epam.lab.spider.controller.command.ActionCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by Marian Voronovskyi on 29.06.2015.
 */
public class GroupsCommand implements ActionCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.getRequestDispatcher("/jsp/admin/admin_groups.jsp").forward(request, response);
    }
}
