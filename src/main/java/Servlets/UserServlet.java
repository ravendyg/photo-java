package Servlets;

import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import Helpers.Utils;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class UserServlet extends HttpServlet {
    private final DBService dbService;
    private final Utils utils;

    public UserServlet(
            DBService dbService,
            Utils utils
    ) {
        this.dbService = dbService;
        this.utils = utils;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;

        String login = req.getHeader("login");
        String password = req.getHeader("pas");

        if (login == null || password == null) {
            response = new ResponseWrapper(null, "Missing data", 400);
            ServletUtils.respond(resp, response);
            return;
        }

        try {
            UsersDataSet usersDataSet = dbService.getUserByName(login);
            if (usersDataSet == null) {
                response = new ResponseWrapper(null, "User not found", 404);
            } else {
                String passwordHash = utils.getPasswordHash(usersDataSet.getUid(), password);
                if (!usersDataSet.matchPassword(passwordHash)) {
                    response = new ResponseWrapper(null, "Incorrect password", 401);
                } else {
                    String token = utils.createGwt(usersDataSet);
                    response = new ResponseWrapper(token, "", 200);
                }
            }
        } catch (DBException e) {
            e.printStackTrace();
            response = new ResponseWrapper(null, "Server error", 500);
        }

        ServletUtils.respond(resp, response);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;

        String login = null;
        String password = null;

        JsonObject body = ServletUtils.parseJsonBody(req);
        try {
            login = body.get("login").getAsString();
            password = body.get("pas").getAsString();
        } catch (Exception e) {
            System.out.println(e);
        }

        if (login == null || password == null) {
            response = new ResponseWrapper(null, "Missing data", 400);
            ServletUtils.respond(resp, response);
            return;
        }

        try {
            UsersDataSet usersDataSet = dbService.getUserByName(login);
            if (usersDataSet != null) {
                response = new ResponseWrapper(null, "User already exists", 409);
            } else {
                dbService.createUser(login, password);
                String token = utils.createGwt(usersDataSet);
                response = new ResponseWrapper(token, "", 200);
            }
        } catch (DBException e) {
            e.printStackTrace();
            response = new ResponseWrapper(null, "Server error", 500);
        }

        ServletUtils.respond(resp, response);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.sendError(HttpServletResponse.SC_NOT_IMPLEMENTED);
    }
}
