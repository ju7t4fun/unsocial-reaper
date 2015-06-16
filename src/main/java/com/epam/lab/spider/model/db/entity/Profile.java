package com.epam.lab.spider.model.db.entity;

import java.util.Date;
import java.util.Set;

/**
 * Created by Boyarsky Vitaliy on 12.06.2015.
 */
public class Profile {

    private Integer id;
    private Integer userId;
    private Integer vkId;
    private String accessToken;
    private Date extTime;
    private Integer appId;
    private Boolean deleted = false;

    private Set<Wall> walls = null;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public Integer getVkId() {
        return vkId;
    }

    public void setVkId(Integer vkId) {
        this.vkId = vkId;
    }

    public String getAccessToken() {
        return accessToken;
    }

    public void setAccessToken(String accessToken) {
        this.accessToken = accessToken;
    }

    public Date getExtTime() {
        return extTime;
    }

    public void setExtTime(Date extTime) {
        this.extTime = extTime;
    }

    public Integer getAppId() {
        return appId;
    }

    public void setAppId(Integer appId) {
        this.appId = appId;
    }

    public Boolean getDeleted() {
        return deleted;
    }

    public void setDeleted(Boolean deleted) {
        this.deleted = deleted;
    }

    @Override
    public String toString() {
        return "Profile{" +
                "id=" + id +
                ", userId=" + userId +
                ", vkId=" + vkId +
                ", accessToken='" + accessToken + '\'' +
                ", extTime=" + extTime +
                ", appId=" + appId +
                ", deleted=" + deleted +
                '}';
    }

}