package com.epam.lab.spider.controller.command.user.auth;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.controller.vk.Parameters;
import com.epam.lab.spider.controller.vk.VKException;
import com.epam.lab.spider.controller.vk.Vkontakte;
import com.epam.lab.spider.controller.vk.api.Users;
import com.epam.lab.spider.controller.vk.auth.AccessToken;
import com.epam.lab.spider.model.entity.Profile;
import com.epam.lab.spider.model.service.ProfileService;
import com.epam.lab.spider.model.service.UserService;
import com.epam.lab.spider.model.vk.User;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

/**
 * Created by Boyarsky Vitaliy on 10.06.2015.
 */
public class VkAuthResponseCommand implements ActionCommand {

    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        System.out.println(request.getParameterMap());
        HttpSession session = request.getSession();
        Vkontakte vk = (Vkontakte) session.getAttribute("siteVk");
        AccessToken token = vk.OAuth().signIn(request);
        session.setAttribute("user_id", token.getUserId());
        session.setAttribute("access_token", token.getAccessToken());
        session.setAttribute("exp_moment", token.getExpirationMoment());

        String name = "some name";
        String surname = "some surname";
        String email ="";

        vk.setAccessToken(token);
        Parameters param = new Parameters();
        param.add("fields","photo_200");


        ProfileService profServ = new ProfileService();
        Profile pro = null;

        try {
            List<User> users = vk.users().get(param);
            User user = users.get(0);
            name = user.getFirstName();
            surname = user.getLastName();
            pro = profServ.getByVkId(user.getId());
        } catch (VKException e) {
            e.printStackTrace();
        }

        boolean existsInDB = false;

        UserService userServ = new UserService();

        com.epam.lab.spider.model.entity.User currUser = null;



        if (pro!= null) {
                int id = pro.getUserId();
                currUser = userServ.getById(id);
        }

        if (currUser!=null) {
            if (!currUser.getDeleted()) {
                existsInDB = true;
            }
        }

        if (existsInDB) {

                boolean isActive = currUser.getConfirm();
                if (!isActive) {
                    request.getSession().setAttribute("loginMessage", "Your account is non-activated." +
                            "Please activate your account via email");
                    request.getSession().setAttribute("login", currUser.getEmail());
                    response.sendRedirect("/login");
                    return;
                }
                request.getSession().setAttribute("user",currUser);
                response.sendRedirect("/");
                return;

        } else {

            request.getSession().setAttribute("name", name);
            request.getSession().setAttribute("surname", surname);
            response.sendRedirect("/register");
            return;

        }
    }
}
