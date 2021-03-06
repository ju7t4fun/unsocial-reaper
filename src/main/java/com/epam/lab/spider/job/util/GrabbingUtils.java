package com.epam.lab.spider.job.util;

import com.epam.lab.spider.integration.vk.VKException;
import com.epam.lab.spider.integration.vk.Vkontakte;
import com.epam.lab.spider.integration.vk.auth.AccessToken;
import com.epam.lab.spider.job.exception.FindingEmptyResultException;
import com.epam.lab.spider.job.exception.WallAlreadyStopped;
import com.epam.lab.spider.job.exception.WallStopException;
import com.epam.lab.spider.job.limit.UserLimitProcessor;
import com.epam.lab.spider.job.limit.UserLimitsFactory;
import com.epam.lab.spider.model.entity.*;
import com.epam.lab.spider.persistence.service.TaskHistoryService;
import com.epam.lab.spider.persistence.service.TaskSynchronizedInfoService;
import com.epam.lab.spider.model.vk.Post;
import org.apache.log4j.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

/**
 * @author Yura Kovalik
 */
public class GrabbingUtils {
    public static final Logger LOG = Logger.getLogger(GrabbingUtils.class);
    static TaskHistoryService synchronizedService = new TaskHistoryService();

    static TaskSynchronizedInfoService syncNewService = new TaskSynchronizedInfoService();

    public static UserLimitProcessor limit = UserLimitsFactory.getUserLimitProcessor();
    public static List<Post> grabbingWall(Wall wall, Task task) throws WallStopException, WallAlreadyStopped, FindingEmptyResultException {
        List<com.epam.lab.spider.model.vk.Post> toPostingQueue = new ArrayList<>();
        Owner owner = wall.getOwner();
        Profile profile = wall.getProfile();
        Filter filter = task.getFilter();
        Set<Integer> alreadyAddSet = synchronizedService.getProcessedPost(task, wall, 10000);
//        Integer grabbingSize = task.getGrabbingSize();
        int countOfPosts = task.getPostCount();
        try {
            Integer appId = profile.getAppId();
            Vkontakte vk = new Vkontakte(appId);
            // Initialization auth_token
            AccessToken accessToken = new AccessToken();
            accessToken.setAccessToken(profile.getAccessToken());
            accessToken.setUserId(profile.getVkId());
            accessToken.setExpirationMoment(profile.getExtTime().getTime());
            vk.setAccessToken(accessToken);
            // !Initialization auth_token
            // Work Body
            switch (task.getGrabbingType()) {

                case BEGIN:
                case END:
                    toPostingQueue =  GrabbingTypeVkSavedSyncUtil.grabbing(task.getGrabbingType(), task.getContentType(),
                            owner, vk, filter, syncNewService.getBy(task, wall), alreadyAddSet, countOfPosts);
                    break;
                case RANDOM:
                    toPostingQueue = GrabbingTypeVkSavedUtil.grabbingRandom(task.getContentType(),owner, vk, filter, alreadyAddSet,
                            countOfPosts);
                    break;
                case NEW:
                    toPostingQueue = GrabbingTypeVkSavedUtil.grabbingNew(task.getContentType(),owner, vk, alreadyAddSet,countOfPosts);
                    break;
            }

            // !Work Body

            LOG.info("TaskJob has successes grab wall#" + wall.getId());
        } catch (RuntimeException x) {
            x.printStackTrace();
        } catch (VKException e) {
            if (e.getExceptionCode() == VKException.VK_AUTHORIZATION_FAILED) {
                Locker.getInstance().lock(profile, DataLock.Mode.AUTH_KEY);
            }
            LOG.error(e.getLocalizedMessage(), e);
        } catch (InterruptedException e) {
            LOG.error(e.getLocalizedMessage(), e);
        }
        return toPostingQueue;
    }

}
