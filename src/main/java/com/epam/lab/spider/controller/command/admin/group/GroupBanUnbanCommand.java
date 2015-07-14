package com.epam.lab.spider.controller.command.admin.group;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.controller.utils.UTF8;
import com.epam.lab.spider.model.db.entity.Category;
import com.epam.lab.spider.model.db.entity.Owner;
import com.epam.lab.spider.model.db.entity.User;
import com.epam.lab.spider.model.db.service.OwnerService;
import com.epam.lab.spider.model.db.service.ServiceFactory;
import com.epam.lab.spider.model.db.service.UserService;
import org.json.JSONObject;
import org.omg.PortableInterceptor.SYSTEM_EXCEPTION;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.ResourceBundle;

/**
 * Created by Орест on 7/2/2015.
 */
public class GroupBanUnbanCommand implements ActionCommand {
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        ResourceBundle bundle = (ResourceBundle) session.getAttribute("bundle");
        String vkid = request.getParameter("vk_id");

        int vk_id = Integer.parseInt(vkid);

        OwnerService service = ServiceFactory.getInstance().create(OwnerService.class);
        JSONObject json = new JSONObject();
        try {
            Owner owner = service.getByVkId(vk_id);
            if (owner == null || owner.getBanned()) {
                if (service.updateUnBan(vk_id)) {
                    json.put("status", "success");
                    json.put("msg", UTF8.encoding(bundle.getString("notification.updated.unbanned.success")));
                } else {
                    json.put("status", "error");
                    json.put("msg", UTF8.encoding(bundle.getString("notification.updated.unbanned.error")));
                }
            } else {
                if (service.updateBan(vk_id)) {
                    json.put("status", "success");
                    json.put("msg", UTF8.encoding(bundle.getString("notification.updated.banned.success")));
                } else {
                    json.put("status", "error");
                    json.put("msg", UTF8.encoding(bundle.getString("notification.updated.banned.error")));
                }
                service.deleteByVkId(vk_id);
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(json.toString());
    }
}