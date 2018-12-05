package Servlets;

import DTO.ImageDTO;
import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import Websockets.DataBus;
import com.google.gson.JsonObject;
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
    private final DBService dbService;
    private final ServletUtils servletUtils;
    private final DataBus dataBus;

    public ImageServlet(
            DBService dbService,
            ServletUtils servletUtils,
            DataBus dataBus
    ) {
        this.dbService = dbService;
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
//
//    @Override
//    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        resp.setContentType("application/json;charset=utf-8");
//        String pathInfo = req.getPathInfo();
//
//        UsersDataSet user = ServletUtils.getUserByName(dbService, req);
//        if (user == null) {
//            JsonObject jo = new JsonObject();
//            jo.addProperty("error", "expired");
//            String joStr = jo.toString();
//            resp.getWriter().println(joStr);
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//        } else if (pathInfo != null && pathInfo.equals((UPLOAD_IMAGE))) {
//            FileOutputStream output = null;
//            try {
//                // TODO: add conversion to "png"
//                String src = utils.getRandom() + ".png";
//
//                String destination = appConfig.getUserPhotoDirectory() + src;
//                InputStream input = req.getInputStream();
//                output = new FileOutputStream(destination);
//                int read;
//                byte[] bytes = new byte[1024];
//                while ((read = input.read(bytes)) != -1) {
//                    output.write(bytes, 0, read);
//                }
//
//                String iid = utils.getUid();
//                PhotoRequest photoRequest = new PhotoRequest(
//                        iid,
//                        "png",
//                        "description",
//                        "title",
//                        user
//                );
//                dbService.addPhoto(photoRequest);
//
//                JsonObject jo = new JsonObject();
//                jo.addProperty("filename", src);
//                String joStr = jo.toString();
//                resp.getWriter().println(joStr);
//                resp.setStatus(HttpServletResponse.SC_OK);
//            } catch (Exception e) {
//                e.printStackTrace();
//                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
//            } finally {
//                if (output != null) {
//                    try {
//                        output.close();
//                    } catch (IOException e) {
//                        e.printStackTrace();
//                    }
//                }
//            }
//        } else {
//            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
//        }
//    }


    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;
        UsersDataSet user = servletUtils.getUser(req);

        if (user != null) {
            String iid;
            try {
                iid = req.getHeader("iid");
                if (iid != null) {
                    boolean deleted = dbService.deletePhoto(iid, user);
                    if (deleted) {
                        response = new ResponseWrapper(null, "", 200);
                        dataBus.broadcastDeletePhoto(iid);
                    } else {
                        response = new ResponseWrapper(null, "Could not delete", 400);
                    }
                } else {
                    response = new ResponseWrapper(null, "Missing iid", 400);
                }
            } catch (DBException e) {
                e.printStackTrace();
                response = new ResponseWrapper(null, "Server error", 500);
            }
        } else {
            response = new ResponseWrapper(null, "Not authenticated", 401);
        }

        servletUtils.respond(resp, response);
    }
}
