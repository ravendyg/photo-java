package Servlets;

import DTO.ResponseWrapper;
import DTO.UserDTO;
import Helpers.AppConfig;
import Helpers.ServletUtils;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private final DBService dbService;
    private final AppConfig appConfig;

    public UserServlet(DBService dbService, AppConfig appConfig) {
        this.dbService = dbService;
        this.appConfig = appConfig;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String name = null;
        String password = null;
        boolean remember = false;

        StringBuffer jb = new StringBuffer();
        String line = null;
        try {
            BufferedReader reader = req.getReader();
            while ((line = reader.readLine()) != null) {
                jb.append(line);
            }
            JsonElement el = (new JsonParser()).parse(jb.toString());
            JsonObject obj = el.getAsJsonObject();
            name = obj.get("login").getAsString();
            password = obj.get("pas").getAsString();
            remember = obj.get("rem").getAsBoolean();
        } catch (Exception e) { /*report an error*/ }

        if (name == null
                || password == null
                || name.matches(".*[^0-1a-zA-Z\\\\s].*")
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
            ResponseWrapper<UserDTO> response = new ResponseWrapper<>(new UserDTO(user), "", 200);

            if (ServletUtils.Respond(resp, response)) {
                return;
            }
        } catch (DBException e) {
            e.printStackTrace();
        }

        resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
