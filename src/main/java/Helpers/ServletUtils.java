package Helpers;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ServletUtils {
    public static void addExpirationCookie(
            DBService dbService,
            UsersDataSet user,
            Boolean remember,
            HttpServletResponse resp
    ) throws DBException {
        String cookieStr = user.getName() + "|" + Utils.getRandom();

        dbService.createSession(user, cookieStr);

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
        if (sessionCookie != null) {
            try {
                user = dbService.getUserBySession(sessionCookie);
            } catch (DBException e) {
                e.printStackTrace();
            }
        }
        return user;
    }

    public static boolean Respond(HttpServletResponse resp, Object response) {
        resp.setContentType("application/json;charset=utf-8");

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        String json = gsonBuilder.create().toJson(response);
        try {
            resp.getWriter().println(json);
            resp.setStatus(HttpServletResponse.SC_OK);
            return true;
        } catch (Exception e) {
            System.out.println(e);
        }
        return false;
    }
}
