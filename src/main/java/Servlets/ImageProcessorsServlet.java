package Servlets;

import DTO.ImageDTO;
import Helpers.AppConfig;
import Helpers.ServletUtils;
import Helpers.Utils;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.*;
import java.util.List;

public class ImageProcessorsServlet extends HttpServlet {
    private final DBService dbService;
    private final AppConfig appConfig;

    private final static String ALL_IMAGES = "/all-images";
    private final static String UPLOAD_IMAGE = "/upload-image";

    public ImageProcessorsServlet(DBService dbService, AppConfig appConfig) {
        this.dbService = dbService;
        this.appConfig = appConfig;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo != null && pathInfo.equals(ALL_IMAGES)) {
            String sessionCookie = ServletUtils.getCookie(req, "uId");
            if (sessionCookie != null) {
                try {
//                    UsersDataSet user = dbService.getUserBySession(sessionCookie);
                    UsersDataSet user = (UsersDataSet) req.getSession().getAttribute("user");
                    List<ImageDTO> images;
                    if (user != null) {
                        images = dbService.getPhotos();
                        GsonBuilder gsonBuilder = new GsonBuilder();
                        gsonBuilder.serializeNulls();
                        String json = gsonBuilder.create().toJson(images);
                        resp.getWriter().println(json);
                        resp.setStatus(HttpServletResponse.SC_OK);
                    }
                } catch (DBException e) {
                    e.printStackTrace();
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String pathInfo = req.getPathInfo();

        UsersDataSet user = ServletUtils.getUser(dbService, req);
        if (user == null) {
            JsonObject jo = new JsonObject();
            jo.addProperty("error", "expired");
            String joStr = jo.toString();
            resp.getWriter().println(joStr);
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        } else if (pathInfo != null && pathInfo.equals((UPLOAD_IMAGE))) {
            FileOutputStream output = null;
            try {
                // TODO: add conversion to "png"
                String src = Utils.getRandom() + ".png";

                String destination = appConfig.getUserPhotoDirectory() + src;
                InputStream input = req.getInputStream();
                output = new FileOutputStream(destination);
                int read;
                byte[] bytes = new byte[1024];
                while ((read = input.read(bytes)) != -1) {
                    output.write(bytes, 0, read);
                }

                dbService.addPhoto(user, src);

                JsonObject jo = new JsonObject();
                jo.addProperty("filename", src);
                String joStr = jo.toString();
                resp.getWriter().println(joStr);
                resp.setStatus(HttpServletResponse.SC_OK);
            } catch (Exception e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } finally {
                if (output != null) {
                    try {
                        output.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
