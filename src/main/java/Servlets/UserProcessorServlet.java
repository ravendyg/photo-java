package Servlets;

import Helpers.ServletUtils;
import dbService.DBService;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserProcessorServlet extends HttpServlet {
    private final static String SIGN_OUT = "/sign_out";

    private final DBService dbService;

    public UserProcessorServlet(DBService dbService) { this.dbService = dbService; }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");
        String pathInfo = req.getPathInfo();

        if (pathInfo.equals(SIGN_OUT)) {
            resp.setStatus(HttpServletResponse.SC_OK);

            ServletUtils.dropCookie(req, resp, "uId");
        } else {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
