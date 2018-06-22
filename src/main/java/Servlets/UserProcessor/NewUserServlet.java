package Servlets.UserProcessor;

import Helpers.AppConfig;
import Helpers.ServletUtils;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

public class NewUserServlet extends HttpServlet {
    private final DBService dbService;
    private final AppConfig appConfig;

    public NewUserServlet(DBService dbService, AppConfig appConfig) {
        this.dbService = dbService;
        this.appConfig = appConfig;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");

        String name = req.getParameter("name");
        String password = req.getParameter("pas");
        String password2 = req.getParameter("pas2");
        String rem = req.getParameter("rem");
        Boolean remember = rem != null && rem.equals("true");

        if (name == null || password == null || password2 == null
                || name.matches(".*[^0-1a-zA-Z\\\\s].*")
                || !password.equals(password2)
                || password.matches("\\s")) {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }

        try {
            UsersDataSet usersDataSet = dbService.getUser(name);
            if (usersDataSet != null) {
                JsonObject jo = new JsonObject();
                jo.addProperty("error", "Already exists");
                String joStr = jo.toString();
                resp.getWriter().println(joStr);

                resp.setStatus(HttpServletResponse.SC_CONFLICT);
                return;
            }

            UsersDataSet user = dbService.createUser(name, password);
            ServletUtils.addExpirationCookie(dbService, user, remember, resp);

            JsonObject jo = new JsonObject();
            jo.addProperty("result", "User created");
            jo.addProperty("name", user.getName());
            String joStr = jo.toString();
            resp.getWriter().println(joStr);

            resp.setStatus(HttpServletResponse.SC_OK);
            return;
        } catch (DBException e) {
            e.printStackTrace();
        }

        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }
}
