package com.epam.lab.spider.controller.vk.auth;

import com.epam.lab.spider.controller.oauth.OAuth;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * Created by Boyarsky Vitaliy on 21.01.2015.
 */
public class AccessToken implements OAuth {

    private String accessToken;
    private Date expirationMoment;
    private int userId;

    @Override
    public String getAccessToken() {
        return accessToken;
    }

    @Override
    public void setAccessToken(String token) {
        this.accessToken = token;
    }

    @Override
    public boolean isExpired() {
        return System.currentTimeMillis() < expirationMoment.getTime();
    }

    @Override
    public Date getExpirationMoment() {
        return expirationMoment;
    }

    @Override
    public void setExpirationMoment(long time) {
        expirationMoment = new Date(System.currentTimeMillis()
                + TimeUnit.SECONDS.toMillis(time));
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int id) {
        this.userId = id;
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append("AccessToken: {")
                .append("user_id='").append(userId).append("', ")
                .append("access_token='").append(accessToken).append("', ")
                .append("expiration_moment='").append(expirationMoment).append("'}");
        return sb.toString();
    }

}
