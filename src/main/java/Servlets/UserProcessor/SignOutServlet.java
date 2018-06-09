package Servlets.UserProcessor;

import Helpers.ServletUtils;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.SessionsDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignOutServlet extends HttpServlet {
    private final DBService dbService;

    public SignOutServlet(DBService dbService) { this.dbService = dbService; }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");

        try {
            for (Cookie cookie : req.getCookies()) {
                if (cookie.getName().equals("uId")) {
                    SessionsDataSet sessionsDataSet = dbService.getSession(cookie.getValue());
                    if (sessionsDataSet != null) {
                        dbService.deleteSession(sessionsDataSet);
                    }
                }
            }
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }

        ServletUtils.dropCookie(req, resp, "uId");
    }
}
