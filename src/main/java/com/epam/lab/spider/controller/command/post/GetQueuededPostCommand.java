package com.epam.lab.spider.controller.command.post;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.model.entity.Attachment;
import com.epam.lab.spider.model.entity.PostingTask;
import com.epam.lab.spider.model.entity.User;
import com.epam.lab.spider.persistence.service.PostingTaskService;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author Boyarsky Vitaliy
 */
public class GetQueuededPostCommand implements ActionCommand {

    private static final Logger LOG = Logger.getLogger(GetPostedPostCommand.class);

    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static PostingTaskService service = factory.create(PostingTaskService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        int offset = Integer.parseInt(request.getParameter("iDisplayStart"));
        int limit = Integer.parseInt(request.getParameter("iDisplayLength"));

        Integer wallId = null;
        try {
            wallId = service.getById(Integer.parseInt(request.getParameter("postId"))).getWallId();
        } catch (NumberFormatException ignored) {
        }

        List<PostingTask> posts = service.getByUserIdWithParameters(user.getId(), offset, limit, "CREATED",
                request.getParameter("sSearch"), request.getParameter("sSortDir_0"), wallId);
        int postCount = service.getCountAllByUserIdWithParameters(user.getId(), "CREATED", request.getParameter
                ("sSearch"), wallId);

        JSONObject result = new JSONObject();
        JSONArray array = new JSONArray();
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm");
        for (PostingTask post : posts) {
            try {
                JSONArray row = new JSONArray();
                row.put(post.getPost().getMessage().length() > 45 ? post.getPost().getMessage().substring(0, 42) +
                        "..." : post.getPost().getMessage());
                row.put(post.getOwner().getName());
                Set<Attachment> attachments = post.getPost().getAttachments();
                if (attachments != null && attachments.size() > 0) {
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
                row.put(dateFormat.format(post.getPostTime()));
                row.put(post.getPostId());
                row.put(post.getId());
                array.put(row);
            } catch (Exception e) {
                LOG.warn("Post id = " + post.getPostId() + " deleted.");
                service.delete(post.getId());
            }
        }

        result.put("iTotalDisplayRecords", postCount);
        result.put("aaData", array);

        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(result.toString());
    }
}
