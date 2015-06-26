package com.epam.lab.spider.model.db.service;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Boyarsky Vitaliy on 12.06.2015.
 */
public class ServiceFactory {

    private static Map<Class<? extends BaseService>, BaseService> services = new HashMap<>();
    private static ServiceFactory instance = null;

    private ServiceFactory() {
        services.put(AttachmentService.class, new AttachmentService());
        services.put(CategoryService.class, new CategoryService());
        services.put(EventService.class, new EventService());
        services.put(FilterService.class, new FilterService());
        services.put(NewPostService.class, new NewPostService());
        services.put(OwnerService.class, new OwnerService());
        services.put(PostService.class, new PostService());
        services.put(TaskService.class, new TaskService());
        services.put(UserService.class, new UserService());
        services.put(ProfileService.class, new ProfileService());
        services.put(WallService.class, new WallService());
        services.put(MessageService.class, new MessageService());
    }

    public static ServiceFactory getInstance() {
        if (instance == null)
            instance = new ServiceFactory();
        return instance;
    }

    public <T extends BaseService> T create(Class<T> serviceClass) {
        return (T) services.get(serviceClass);
    }

}
