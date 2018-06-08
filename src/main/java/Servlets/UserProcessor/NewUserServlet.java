package Servlets.UserProcessor;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class NewUserServlet extends HttpServlet {
    private final DBService dbService;

    public NewUserServlet(DBService dbService) {
        this.dbService = dbService;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
                resp.sendError(HttpServletResponse.SC_CONFLICT);
                return;
            }

            UsersDataSet user = dbService.createUser(name, password);
            System.out.println(user.toString());

            JsonObject jo = new JsonObject();
            jo.addProperty("result", "User created");
            jo.addProperty("name", user.getName());
            String joStr = jo.toString();
            resp.getWriter().println(joStr);
            resp.setContentType("application/json;charset=utf-8");
            resp.setStatus(HttpServletResponse.SC_OK);
        } catch (DBException e) {
            e.printStackTrace();
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
        }
    }
}
