package Servlets.UserProcessor;

import Helpers.ServletUtils;
import dbService.DBService;

import javax.servlet.ServletException;
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

        resp.setStatus(HttpServletResponse.SC_OK);

        ServletUtils.dropCookie(req, resp, "uId");
    }
}
