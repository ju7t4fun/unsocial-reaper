package com.epam.lab.spider.controller.command.category;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.persistence.service.CategoryService;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Dzyuba Orest
 */
public class RemoveCategoryCommand implements ActionCommand {

    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static CategoryService service = factory.create(CategoryService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ResourceBundle bundle = (ResourceBundle) session.getAttribute("bundle");
        int id = Integer.parseInt(request.getParameter("id"));
        JSONObject json = new JSONObject();
        if (service.delete(id)) {
            json.put("status", "success");
            json.put("msg", "Successfully deleted!");
        } else {
            json.put("status", "error");
            json.put("msg", "Occured error!");
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
}

