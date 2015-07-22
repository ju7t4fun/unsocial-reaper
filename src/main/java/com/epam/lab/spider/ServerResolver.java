package com.epam.lab.spider;

import javax.servlet.http.HttpServletRequest;

/**
 * Created by hell-engine on 7/16/2015.
 */
public class ServerResolver {
    public static String getServerPath(HttpServletRequest request){
        // default "http://localhost:8080"
        String path = "http://"+request.getServerName();
        if(request.getServerPort() != 80){
            path += ":"+request.getServerPort();
        }
        path += request.getContextPath();
        return path;
    }
}
