package com.epam.lab.spider.integration.vk.api;

import com.epam.lab.spider.integration.vk.Parameters;
import com.epam.lab.spider.integration.vk.Response;
import com.epam.lab.spider.integration.vk.VKException;
import com.epam.lab.spider.integration.vk.auth.AccessToken;
import com.epam.lab.spider.model.vk.Link;

import java.util.Date;

public class Utils extends Methods {

    public Utils(AccessToken token) {
        super(token);
    }

    /**
     * Возвращает информацию о том, является ли внешняя ссылка заблокированной на сайте ВКонтакте.
     */
    public Link.Status checkLink(Parameters param) throws VKException {
        Response response = request("utils.checkLink", param).execute();
        return Link.Status.getByName(response.root().child("status").get(0).value().toString());
    }

    /**
     * Определяет тип объекта (пользователь, сообщество, приложение) и его идентификатор по короткому имени screen_name.
     */
    public ScreenName resolveScreenName(Parameters param) throws VKException {
        final Response response = request("utils.resolveScreenName", param).execute();
//        return response.root().child("object_id").get(0).value().toInt();
        return new ScreenName() {
            @Override
            public Type getType() {
                return Type.valueOf(response.root().child("type").get(0).value().toString().toUpperCase());
            }

            @Override
            public int getObjectId() {
                return response.root().child("object_id").get(0).value().toInt();
            }
        };
    }

    public interface ScreenName {
        enum Type {
            USER, GROUP, APPLICATION, PAGE
        }

        Type getType();

        int getObjectId();
    }

    /**
     * Определяет тип объекта (пользователь, сообщество, приложение) и его идентификатор по короткому имени screen_name.
     * для груп -id
     * для пользователей id
     */
    public int resolveScreenNameEx(String domain) throws VKException {
        Parameters param = new Parameters();
        param.add("screen_name", domain);
        final Response response = request("utils.resolveScreenName", param).execute();
        Integer id = response.root().child("object_id").get(0).value().toInt();
        boolean isGroup = response.root().child("type").get(0).value().toString().equals("group");
        return isGroup ? -id : id;
    }

    /**
     * Возвращает текущее время на сервере ВКонтакте.
     */
    public Date getServerTime() throws VKException {
        Response response = request("utils.getServerTime", new Parameters()).execute();
        return response.root().value().toDate();
    }

}
