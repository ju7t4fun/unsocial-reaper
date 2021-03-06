package com.epam.lab.spider.job.util;

import com.epam.lab.spider.integration.vk.Parameters;
import com.epam.lab.spider.integration.vk.VKException;
import com.epam.lab.spider.integration.vk.Vkontakte;
import com.epam.lab.spider.model.entity.Owner;
import com.epam.lab.spider.model.vk.Group;
import com.epam.lab.spider.model.vk.User;
import com.epam.lab.spider.persistence.service.OwnerService;
import com.epam.lab.spider.persistence.service.ServiceFactory;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Yura Kovalik
 */
public class OpenedOwnerUpdater {
    public static final Logger LOG = Logger.getLogger(OpenedOwnerUpdater.class);
    private static final ServiceFactory factory = ServiceFactory.getInstance();
    private static final OwnerService ownerService = factory.create(OwnerService.class);

    public static void updateAllOwners(){
        int changeCount = 0;
        List<Owner> owners = ownerService.getLimited(0, 1000);
        StringBuilder usersStringBuilder, groupStringBuilder ;
        usersStringBuilder = new StringBuilder();
        groupStringBuilder = new StringBuilder();
        List<Owner> usersWall = new ArrayList<>();
        List<Owner> groupsWall = new ArrayList<>();
        for(Owner owner:owners){
            Integer id = owner.getVkId();
            if(id>0){
                usersWall.add(owner);
                usersStringBuilder.append(id).append(",");
            }else {
                groupsWall.add(owner);
                groupStringBuilder.append(-id).append(",");
            }
        }
        Vkontakte vkontakte = new Vkontakte();

        
        

        Parameters parameters;
        // for users
        parameters = new Parameters();
        parameters.add("fields","domain");
        parameters.add("user_ids",usersStringBuilder.substring(0, usersStringBuilder.length()-1));
        try {
            List<User> users = vkontakte.users().get(parameters);
            if(users.size()!=usersWall.size()){
                LOG.fatal("Unequals lists size!");
            }else
            for (int i = 0; i < users.size(); i++) {
                User user = users.get(i);
                Owner owner = usersWall.get(i);

                if(user.getId() != owner.getVkId().intValue()){
                    LOG.error("Unequals vk.id's!");
                }else{
                    boolean change = false;
                    if(!user.getDomain().equals(owner.getDomain())){
                        change = true;
                        owner.setDomain(user.getDomain());
                    }
                    String ownerName = user.getFirstName()+" "+user.getLastName();
                    if(!ownerName.equals(owner.getName())){
                        change = true;
                        owner.setName(ownerName);
                    }
                    if(change){
                        changeCount++;
                        ownerService.update(owner.getId(),owner);
                    }
                }
            }
        } catch (VKException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        // for groups
        parameters = new Parameters();
        parameters.add("group_ids",groupStringBuilder.substring(0, groupStringBuilder.length()-1));
        try {
            List<Group> groups = vkontakte.groups().getById(parameters);
            if(groups.size()!=groupsWall.size()){
                LOG.fatal("Unequals lists size!");
            }else
                for (int i = 0; i < groups.size(); i++) {
                    Group group = groups.get(i);
                    Owner owner = groupsWall.get(i);
                    Integer groupId = - group.get("id").toInt();
                    boolean closed = group.get("is_closed").toInt()>0;
                    if(closed){
                        LOG.info("Closed group. Field not updated!");
                        continue;
                    }
                    if(groupId.intValue() != owner.getVkId().intValue()){
                        LOG.error("Unequals vk.id's!");
                    }else{
                        boolean change = false;
                        String domain = group.get("screen_name").toString();
                        if(!domain.equals(owner.getDomain())){
                            change = true;
                            owner.setDomain(domain);
                        }
                        String ownerName = group.get("name").toString();
                        if(!ownerName.equals(owner.getName())){
                            change = true;
                            owner.setName(ownerName);
                        }
                        if(change){
                            changeCount++;
                            ownerService.update(owner.getId(),owner);
                        }
                    }
                }
        } catch (VKException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }

        LOG.info("Updated data of "+changeCount+" owners.");
    }
    public static void main(String[] args){
        updateAllOwners();
    }
}
