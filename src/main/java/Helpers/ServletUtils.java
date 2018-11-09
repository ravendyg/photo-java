package Helpers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;

public class ServletUtils {
    public static void addExpirationCookie(
            DBService dbService,
            UsersDataSet user,
            Boolean remember,
            HttpServletResponse resp
    ) throws DBException {
        String cookieStr = user.getName() + "|" + Utils.getRandom();

        Cookie cookie = new Cookie("uId", cookieStr);
        cookie.setPath("/");
        cookie.setMaxAge(remember ? Utils.TEN_DAYS : -1);
        resp.addCookie(cookie);
    }

    public static void dropCookie(HttpServletRequest req, HttpServletResponse resp, String key) {
        Cookie cookie = new Cookie(key, null);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        resp.addCookie(cookie);
    }

    public static String getCookie(HttpServletRequest req, String key) {
        Cookie[] cookies = req.getCookies();
        if (cookies == null) {
            return null;
        }
        for (Cookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }

        return null;
    }

    public static UsersDataSet getUser(DBService dbService, HttpServletRequest req) {
        String sessionCookie = ServletUtils.getCookie(req, "uId");
        UsersDataSet user = null;
        return user;
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
