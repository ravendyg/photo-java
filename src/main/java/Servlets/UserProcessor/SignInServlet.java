package Servlets.UserProcessor;

import Helpers.ServletUtils;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class SignInServlet extends HttpServlet {
    private final DBService dbService;

    public SignInServlet(DBService dbService) { this.dbService = dbService; }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json;charset=utf-8");

        String name = req.getParameter("name");
        String password = req.getParameter("pas");
        String rem = req.getParameter("rem");
        Boolean remember = rem != null && rem.equals("true");

        if (name != null && password != null) {
            try {
                UsersDataSet user = dbService.getUser(name);
                if (user == null) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("error", "Wrong username");
                    resp.getWriter().println(jo.toString());

                    resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
                } else if (!user.matchPassword(password)) {
                    JsonObject jo = new JsonObject();
                    jo.addProperty("error", "Wrong password");
                    resp.getWriter().println(jo.toString());

                    resp.setStatus(HttpServletResponse.SC_FORBIDDEN);
                } else {

                    JsonObject jo = new JsonObject();
                    jo.addProperty("result", "LogedIn");
                    jo.addProperty("name", name);
                    resp.getWriter().println(jo.toString());

                    resp.setStatus(HttpServletResponse.SC_OK);
                }
            } catch (DBException e) {
                e.printStackTrace();
                resp.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            }
        } else {
            resp.sendError(HttpServletResponse.SC_BAD_REQUEST);
        }
    }
}
