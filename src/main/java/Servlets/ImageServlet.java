package Servlets;

import DTO.ImageDTO;
import DTO.ResponseWrapper;
import Data.PhotoRequest;
import Helpers.AppConfig;
import Helpers.ServletUtils;
import Helpers.Utils;
import AsyncHandlers.DataBus;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class ImageServlet extends HttpServlet {
    private final AppConfig appConfig;
    private final DBService dbService;
    private final Utils utils;
    private final ServletUtils servletUtils;
    private final DataBus dataBus;

    public ImageServlet(
            AppConfig appConfig,
            DBService dbService,
            Utils utils,
            ServletUtils servletUtils,
            DataBus dataBus
    ) {
        this.appConfig = appConfig;
        this.dbService = dbService;
        this.utils = utils;
        this.servletUtils = servletUtils;
        this.dataBus = dataBus;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;
        UsersDataSet usersDataSet = servletUtils.getUser(req);

        if (usersDataSet != null) {
            try {
                List<ImageDTO> images;
                images = dbService.getPhotos(usersDataSet);
                response = new ResponseWrapper(images, "", 200);
            } catch (DBException e) {
                e.printStackTrace();
                response = new ResponseWrapper(null, "Server error", 500);
            }
        } else {
            response = new ResponseWrapper(null, "Not authenticated", 401);
        }

        servletUtils.respond(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;
        UsersDataSet user = servletUtils.getUser(req);

        if (user != null) {
            try {
                String title = req.getHeader("title");
                String description = req.getHeader("description");

                FileOutputStream output = null;
                String iid = utils.getUid();
                String src = iid + ".png";
                String destination = appConfig.getUserPhotoDirectory() + src;
                InputStream input = req.getInputStream();
                output = new FileOutputStream(destination);
                int read;
                byte[] bytes = new byte[1024];
                while ((read = input.read(bytes)) != -1) {
                    output.write(bytes, 0, read);
                }

                PhotoRequest photoRequest = new PhotoRequest(
                        iid,
                        "png",
                        description,
                        title,
                        user
                );
                ImageDTO imageDTO = dbService.addPhoto(user, photoRequest);

                response = new ResponseWrapper(null, null, 200);
                dataBus.broadcastNewPhoto(imageDTO);
            } catch (Exception e) {
                e.printStackTrace();
                response = new ResponseWrapper(null, "Server error", 500);
            }
        } else {
            response = new ResponseWrapper(null, "Not authenticated", 401);
        }

        servletUtils.respond(resp, response);
    }

}
