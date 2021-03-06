package com.epam.lab.spider.controller.servlet.admin;

import com.epam.lab.spider.controller.command.ActionFactory;
import com.epam.lab.spider.controller.command.admin.category.CategoriesCommand;
import com.epam.lab.spider.controller.command.category.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;

/**
 * @author Marian Voronovskyi
 */
public class CategoriesServlet extends HttpServlet {
    private static ActionFactory factory = new CategoriesFactory();

    private static class CategoriesFactory extends ActionFactory {

        public CategoriesFactory() {
            commands = new HashMap<>();
            commands.put("default", new CategoriesCommand());
            commands.put("getcategory", new GetCategoryCommand());
            commands.put("addcategory", new AddCategoryCommand());
            commands.put("removecategory", new RemoveCategoryCommand());
            commands.put("upCat", new UploadCatImage());
            commands.put("editCat", new EditCategory());
        }
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        factory.action(request, response);
    }

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        factory.action(request, response);
    }
}
