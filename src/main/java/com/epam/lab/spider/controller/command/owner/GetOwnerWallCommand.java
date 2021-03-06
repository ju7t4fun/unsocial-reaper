package com.epam.lab.spider.controller.command.owner;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.model.entity.User;
import com.epam.lab.spider.model.entity.Wall;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import com.epam.lab.spider.persistence.service.WallService;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Oleksandra Lobanok
 */
public class GetOwnerWallCommand implements ActionCommand {
    private static final Logger LOG = Logger.getLogger(GetOwnerWallCommand.class);

    private static ServiceFactory factory = ServiceFactory.getInstance();
    private static WallService service = factory.create(WallService.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");
        List<Wall> writeWalls;
        if (user.getRole() == User.Role.USER) {
          writeWalls = service.getWriteByUserId(user.getId());
        } else {
            writeWalls = service.getWriteByAdmin();
        }
        List<OwnerWall> ownerWalls = new ArrayList<>();
        for (final Wall wall : writeWalls) {
            ownerWalls.add(new OwnerWall() {
                @Override
                public int getWallId() {
                    return wall.getId();
                }

                @Override
                public String getName() {
                    return wall.getOwner().getName() + " (" + wall.getProfile().getName() + ")";
                }
            });
        }
        JSONArray row = new JSONArray();
        for (OwnerWall owner : ownerWalls) {

            JSONObject o = new JSONObject();
            o.put("id", owner.getWallId());
            o.put("name", owner.getName());
            row.put(o);

        }
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        JSONObject obj = new JSONObject();
        {
            // Формування дати
            SimpleDateFormat date = new SimpleDateFormat("yyyy-MM-dd");
            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
            Date now = new Date();
            String currentDate = date.format(now);
            String currentTime = time.format(now);
            obj.put("date", currentDate);
            obj.put("time", currentTime);

            now = new Date(now.getTime() + 60 * 60 * 1000);
            currentDate = date.format(now);
            currentTime = time.format(now);
            obj.put("del_date", currentDate);
            obj.put("del_time", currentTime);
        }
        obj.put("owner", row);
        LOG.debug(obj);
        response.getWriter().write(obj.toString());
    }

    public interface OwnerWall {

        int getWallId();

        String getName();

    }
}
