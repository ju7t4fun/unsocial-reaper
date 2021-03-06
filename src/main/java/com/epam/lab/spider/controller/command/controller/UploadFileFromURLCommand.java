package com.epam.lab.spider.controller.command.controller;

import com.epam.lab.spider.controller.command.ActionCommand;
import com.epam.lab.spider.controller.utils.FileType;
import com.epam.lab.spider.controller.utils.UTF8;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.log4j.Logger;
import org.json.JSONObject;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.security.SecureRandom;
import java.util.Map;
import java.util.ResourceBundle;

/**
 * @author Marian Voronovskyi
 */
public class UploadFileFromURLCommand implements ActionCommand {
    private static final Logger LOG = Logger.getLogger(UploadFileFromURLCommand.class);

    @Override
    public void execute(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        HttpSession session = request.getSession();
        ResourceBundle bundle = (ResourceBundle) session.getAttribute("bundle");

        ServletContext context = request.getSession().getServletContext();
        response.setContentType("application/json");
        Map<String, String> urlType = (Map<String, String>) request.getSession().getAttribute("files_url");

        String filePath;
        String message;
        String status;
        String fileName;
        try {
            String urlFromAjax = request.getParameter("url");
            URL url = new URL(urlFromAjax);
            String extension = "." + FilenameUtils.getExtension(url.toString());
            FileType.Type type = FileType.getType(extension);
            filePath = context.getRealPath(type.getPath());
            SecureRandom random = new SecureRandom();
            fileName = new BigInteger(130, random).toString(32) +
                    extension;
            File file = new File(filePath);
            if (!file.exists()) {
                file.mkdirs();
            }
            FileUtils.copyURLToFile(url, new File(filePath + File.separator + fileName));
            urlType.put(type.getPath() + "/" + fileName, type.toString());
            status = "success";
            message = UTF8.encoding(bundle.getString("notification.upload.file.success"));
        } catch (Exception e) {
            LOG.error(e.getLocalizedMessage(), e);
            status = "fail";
            message = UTF8.encoding(bundle.getString("notification.upload.url.error"));
        }
        if (!urlType.isEmpty()) {
            LOG.debug(urlType);
            request.getSession().setAttribute("files_url", urlType);
        }
        response.getWriter().print(new JSONObject().put(status, message));
    }
}
