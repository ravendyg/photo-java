package Helpers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class ServletUtils {
    private final DBService dbService;
    private final Utils utils;

    public ServletUtils(DBService dbService, Utils utils) {
        this.dbService = dbService;
        this.utils = utils;
    }

    public UsersDataSet getUser(HttpServletRequest req) {
        String token = req.getHeader("token");
        return getUser(token);
    }

    public UsersDataSet getUser(String token) {
        if (token == null) {
            return null;
        }

        String uid = utils.getUidFromGwt(token);
        if (uid == null) {
            return null;
        }

        try {
            return dbService.getUserByUid(uid);
        } catch (DBException e) {}

        return null;
    }

    public static void respond(HttpServletResponse resp, Object response) {
        resp.setContentType("application/json;charset=utf-8");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        String json = gsonBuilder.create().toJson(response);
        try {
            resp.getWriter().println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (Exception e) {
            try {
                resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            } catch (Exception er) {
                System.out.println(er);
            }
            System.out.println(e);
        }
    }

    public static JsonObject parseJsonBody(HttpServletRequest req) {
        JsonObject obj = null;
        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
            JsonElement el = (new JsonParser()).parse(jb.toString());
            obj = el.getAsJsonObject();
        } catch (Exception e) {
            System.out.println(e);
        }

        return obj;
    }
}
