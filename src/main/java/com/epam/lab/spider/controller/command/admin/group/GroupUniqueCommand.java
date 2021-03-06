package com.epam.lab.spider.controller.command.admin.group;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.model.entity.Owner;
import com.epam.lab.spider.persistence.service.OwnerService;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import org.apache.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * @author Dzyuba Orest
 */
public class GroupUniqueCommand implements ActionCommand {
    private static final Logger LOG = Logger.getLogger(GroupUniqueCommand.class);
    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String toSearch = request.getParameter("sSearch");
        String pageNo = request.getParameter("iDisplayStart");
        String pageSize = request.getParameter("iDisplayLength");


        OwnerService service = ServiceFactory.getInstance().create(OwnerService.class);

        int start = 0;
        int ammount = 5;

        if (pageNo!=null) {
            start = Integer.parseInt(pageNo);
            if (start < 0) {
                start = 0;
            }
        }

        if (pageSize!=null) {
            ammount = Integer.parseInt(pageSize);
            if (ammount < 5) {
                ammount = 5;
            }
        }

        int totalCount;
        List<Owner> owners;
        if (toSearch == null || toSearch.isEmpty()) {
            owners = service.getAllGroups(start, ammount);
            totalCount = service.getCountAllUnique();
        } else {
            toSearch = "%" + toSearch + "%";
            LOG.info("toSearch: " + toSearch);
            owners = service.getAllGroupsWithSearch(toSearch, start, ammount);
            totalCount = service.getCountAllUniqueWithSearch(toSearch);
        }
        JSONObject result = new JSONObject();
        JSONArray table = new JSONArray();
        if (owners!=null) {
            for (Owner owner : owners) {
                try {
                    JSONArray row = new JSONArray();
                    row.put(owner.getVkId());
                    row.put(owner.getName());
                    row.put(owner.getBanned());
                    table.put(row);
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
        LOG.info(owners.size());
        LOG.info(totalCount);

        result.put("iTotalDisplayRecords", totalCount);
        result.put("aaData", table);
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");
        response.getWriter().print(result.toString());
    }
}
