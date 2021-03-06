package com.epam.lab.spider.controller.command.post;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.model.entity.Attachment;
import com.epam.lab.spider.model.entity.Post;
import com.epam.lab.spider.model.entity.User;
import com.epam.lab.spider.persistence.service.PostService;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Boyarsky Vitaliy
 */
public class GetCreatedPostCommand implements ActionCommand {

    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static PostService service = factory.create(PostService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int page = Integer.parseInt(request.getParameter("iDisplayStart"));
        int size = Integer.parseInt(request.getParameter("iDisplayLength"));

        List<Post> posts;
        int postCount;
        String nameToSearch = request.getParameter("sSearch");
        if (nameToSearch != null && !nameToSearch.isEmpty()) {
            nameToSearch = "%" + nameToSearch + "%";
            postCount = service.getCountByUserIdWithSearch(user.getId(), nameToSearch);
            posts = service.getByUserIdWithSearch(user.getId(), page, size, nameToSearch);
        } else {
            posts = service.getByUserId(user.getId(), page, size);
            postCount = service.getCountByUserId(user.getId());
        }

        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();

        if (posts != null) {
            for (Post post : posts) {
                JSONArray row = new JSONArray();
                row.put(post.getId());
                row.put(post.getMessage().length() > 75 ? post.getMessage().substring(0, 72) + "..." : post
                        .getMessage());
                Set<Attachment> attachments = post.getAttachments();
                if (attachments.size() > 0) {
                    Map<Attachment.Type, Integer> attachmentCount = new HashMap<>();
                    for (Attachment attachment : attachments) {
                        int count = 0;
                        if (attachmentCount.containsKey(attachment.getType())) {
                            count = attachmentCount.get(attachment.getType());
                        }
                        count++;
                        attachmentCount.put(attachment.getType(), count);
                    }
                    String group = null;
                    for (Attachment.Type type : attachmentCount.keySet()) {
                        group = group == null ? "" + type + "|" + attachmentCount.get(type) : group + "!" + type +
                                "|" + attachmentCount.get(type);
                    }
                    row.put(group);
                } else {
                    row.put("");
                }
                row.put(post.getId());
                array.put(row);
            }
        }

        result.put("iTotalDisplayRecords", postCount);
        result.put("aaData", array);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(result.toString());
    }

}

