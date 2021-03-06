package com.epam.lab.spider.job.util;

import com.epam.lab.spider.controller.utils.Receiver;
import com.epam.lab.spider.controller.utils.Sender;
import com.epam.lab.spider.model.entity.Category;
import com.epam.lab.spider.model.entity.Post;
import com.epam.lab.spider.model.entity.Task;
import com.epam.lab.spider.persistence.service.CategoryService;
import com.epam.lab.spider.persistence.service.PostService;
import com.epam.lab.spider.persistence.service.ServiceFactory;

import java.util.List;

/**
 * @author Boyarsky Vitaliy
 */
public class Feed {

    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static PostService postService = factory.create(PostService.class);
    private static CategoryService categoryService = factory.create(CategoryService.class);
    private static Receiver feedReceiver = null;

    public boolean processing(Post post, Task task) {
        List<Category> categories = categoryService.getByTaskId(task.getId());
        post.setUserId(-1);
        if (postService.insert(post)) {
            postService.bindWithCategories(post.getId(), categories);
            if (feedReceiver != null)
                feedReceiver.send(post.getId(), "new|" + post.getId());
        }
        return false;
    }

    public static class FeedSender implements Sender {

        @Override
        public void accept(Receiver receiver) {
            feedReceiver = receiver;
        }

        @Override
        public void history(int clientId) {

        }
    }

}
