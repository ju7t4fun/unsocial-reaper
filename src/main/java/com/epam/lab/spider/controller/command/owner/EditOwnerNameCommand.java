package com.epam.lab.spider.controller.command.owner;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.controller.utils.ReplaceHtmlTags;
import com.epam.lab.spider.controller.utils.UTF8;
import com.epam.lab.spider.model.entity.Owner;
import com.epam.lab.spider.model.entity.User;
import com.epam.lab.spider.persistence.service.OwnerService;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * @author Marian Voronovskyi
 */
public class EditOwnerNameCommand implements ActionCommand {
    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static OwnerService service = factory.create(OwnerService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int ownerId = Integer.parseInt(request.getParameter("id"));
        User user = (User) request.getSession().getAttribute("user");
        String name = request.getParameter("name");
        name = ReplaceHtmlTags.reaplaceAll(name);
        HttpSession session = request.getSession();
        ResourceBundle bundle = (ResourceBundle) session.getAttribute("bundle");
        JSONObject json = new JSONObject();
        Owner owner = service.getById(ownerId);
        owner.setName(name);
        owner.setUserId(user.getId());
        if (service.update(ownerId, owner)) {
            json.put("status", "success");
            json.put("msg", UTF8.encoding(bundle.getString("notification.edit.owner.name.success")));
        } else {
            json.put("status", "error");
            json.put("msg", UTF8.encoding(bundle.getString("notification.edit.owner.name.error")));
        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
}
