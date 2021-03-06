package com.epam.lab.spider.controller.command.admin;

import com.epam.lab.spider.ServerLocationUtils;
import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.controller.command.task.SaveTaskCommand;
import com.epam.lab.spider.model.entity.Task;
import com.epam.lab.spider.model.entity.User;
import com.epam.lab.spider.model.entity.Wall;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import com.epam.lab.spider.persistence.service.TaskService;
import com.epam.lab.spider.persistence.service.WallService;
import org.apache.log4j.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author Marian Voronovskyi
 */
public class AdminEditTaskCommand implements ActionCommand {
    private final static Logger LOG = Logger.getLogger(SaveTaskCommand.class);
    private static ServiceFactory factory = ServiceFactory.getInstance();
    private TaskService taskService = factory.create(TaskService.class);
    private WallService wallService =  factory.create(WallService.class);
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        User user = null;
        {
            Object userObject = request.getSession().getAttribute("user");
            if(userObject!=null && userObject instanceof User)user = (User) userObject;
            if(user==null) {
                response.sendError(401);
                return;
            }
        }
        try {
            String taskIdString = request.getParameter("taskId");
            Integer index = Integer.parseInt(taskIdString);

            Task task;
            if (user.getRole() == User.Role.ADMIN) {
                task = taskService.getByIdNoLimit(index);
            } else {
                task = taskService.getByIdAndLimitByUserId(index, user.getId());
            }

            request.setAttribute("task_id",taskIdString);

            List<Wall> walls = wallService.getAllByUserID(user.getId());
            Map<String, String> wallMap;
            List<Map<String,String>> sourceWalls = new ArrayList<>();
            for (Wall wall : walls) {
                if(wall.getPermission()!= Wall.Permission.READ)continue;
                wallMap = new HashMap<>();
                wallMap.put("id", wall.getId().toString());
                if(task.getSource().contains(wall)){
                    wallMap.put("selected", "true");
                }else{
                    wallMap.put("selected", "false");
                }
                wallMap.put("text", wall.getOwner().getName());
                sourceWalls.add(wallMap);
            }
            request.setAttribute("sourceWalls",sourceWalls);



            request.setAttribute("posting_type",task.getType());
            request.setAttribute("grabbing_type",task.getGrabbingType());
            request.setAttribute("repeat", task.getRepeat());
            request.setAttribute("repeat_count", task.getRepeatCount());

            request.setAttribute("signature", task.getSignature());

            request.setAttribute("start_time",task.getStartTimeType());
            request.setAttribute("work_time", task.getWorkTimeLimit());


            request.setAttribute("interval_min", task.getIntervalMin());
            request.setAttribute("interval_max", task.getIntervalMax());
            request.setAttribute("post_count", task.getPostCount());
            request.setAttribute("post_delay_min", task.getPostDelayMin());
            request.setAttribute("post_delay_max", task.getIntervalMax());
            request.setAttribute("grabbing_mode", task.getGrabbingMode());

            request.setAttribute("likes", task.getFilter().getLikes());
            request.setAttribute("reposts", task.getFilter().getRePosts());
            request.setAttribute("comments", task.getFilter().getComments());

            StringBuilder hashTagsStringBuilder = new StringBuilder();

            String hashs[] = task.getHashTags().replaceAll("#","").split(" ");
            for(String hash:hashs){
                hashTagsStringBuilder.append(", ").append(hash);
            }
            String hashTags;
            if(hashTagsStringBuilder.length()>2)hashTags = hashTagsStringBuilder.substring(2);
            else hashTags = "";
            request.setAttribute("hashtags", hashTags);

            if(task.getContentType().hasText()){request.setAttribute("TEXT","true");}
            if(task.getContentType().hasPhoto()){request.setAttribute("PHOTO","true");}
            if(task.getContentType().hasAudio()){request.setAttribute("AUDIO","true");}
            if(task.getContentType().hasVideo()){request.setAttribute("VIDEO","true");}
            if(task.getContentType().hasDoc()){request.setAttribute("DOCUMENTS","true");}
            if(task.getContentType().hasHashTags()){request.setAttribute("HASH_TAGS","true");}
            if(task.getContentType().hasLinks()){request.setAttribute("LINKS","true");}
            if(task.getContentType().hasPages()){request.setAttribute("PAGES","true");}
            if(task.getContentType().hasRePosts()){request.setAttribute("RE_POSTS","true");}
            if(task.getContentType().hasSimpleTitle()){request.setAttribute("SIMPLE_TITLE","true");}
            if(task.getContentType().hasTextTitle()){request.setAttribute("TEXT_TITLE","true");}

            request.setAttribute("likes_max","1200");
            request.setAttribute("reposts_max","600");
            request.setAttribute("comments_max","300");
            request.setAttribute("delay_limit","120");
            request.setAttribute("interval_limit","120");


            request.getRequestDispatcher("jsp/task/edit-task-master.jsp").forward(request, response);

        }catch (RuntimeException x){
            LOG.error(x);
            response.sendRedirect(ServerLocationUtils.getServerPath(request)+"/task?action=create");
        }
    }
}
