package com.epam.lab.spider.controller.command.user.auth;

import com.epam.lab.spider.ServerLocationUtils;
import com.epam.lab.spider.controller.command.ActionCommand;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;

/**
 * @author Boyarsky Vitaliy
 */
public class SignOutCommand implements ActionCommand {

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        session.invalidate();
        response.sendRedirect(ServerLocationUtils.getServerPath(request)+"/");
    }
}
