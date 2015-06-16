package com.epam.lab.spider.job;

import com.epam.lab.spider.controller.vk.Parameters;
import com.epam.lab.spider.controller.vk.VKException;
import com.epam.lab.spider.controller.vk.Vkontakte;
import com.epam.lab.spider.controller.vk.auth.AccessToken;
import com.epam.lab.spider.model.entity.*;
import com.epam.lab.spider.model.service.*;
import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

/**
 * Created by shell on 6/16/2015.
 */
public class OnePostJob implements Job {
    public static final Logger LOG = Logger.getLogger(OnePostJob.class);

    NewPostService newPostService = new NewPostService();
    PostService postService = new PostService();
    WallService wallService = new WallService();
    OwnerService ownerService = new OwnerService();
    ProfileService profileService = new ProfileService();

    PostMetadataService metadataService = new PostMetadataService();





    @Override
    public void execute(JobExecutionContext jobExecutionContext) throws JobExecutionException {
        JobDataMap dataMap = jobExecutionContext.getJobDetail().getJobDataMap();
        NewPost newPost = null;
        {
            Integer id = dataMap.getInt("new_post_id");
            newPost = newPostService.getById(id);
        }
        if(newPost==null){
            LOG.error("Quartz failed. Have not new_post_id in DataMap!");
            return;
        }
        try{
            newPost.setPost(postService.getById(newPost.getPost().getId()));
            Wall wall = wallService.getById(newPost.getWallId());


            Owner owner = ownerService.getById(wall.getOwner_id());

            Profile profile = profileService.getById(wall.getProfile_id());

            Vkontakte vk = new Vkontakte(4949213);


            // да здраствует безумие!!!!
            AccessToken accessToken = new AccessToken();
            accessToken.setAccessToken(profile.getAccessToken());
            accessToken.setUserId(profile.getVkId());
            accessToken.setExpirationMoment(profile.getExtTime().getTime());
            vk.setAccessToken(accessToken);
            //слава Ктулху!!!


            Parameters parameters = new Parameters();
            parameters.add("owner_id",owner.getVk_id());
            parameters.add("message",newPost.getPost().getMessage());
            long response = 0;
            if(true) {
                response = vk.wall().post(parameters);
            }
            PostMetadata metadata = new PostMetadata();
            metadata.setLike(0);
            metadata.setRepost(0);

            metadataService.insert(metadata);

            newPost.setMetadata(metadata);
            newPost.setState(NewPost.State.POSTED);
            newPostService.update(newPost.getId(),newPost);

            LOG.debug("new post success : " +owner.getVk_id()+"_"+response);
        }catch (NullPointerException|VKException x){
            LOG.error("Posting has failed. Corrupted new_post #" + newPost.getId());
            x.printStackTrace();
        }
        System.out.println(newPost.getPost().getMessage());
    }
}