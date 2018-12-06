package Servlets;

import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import AsyncHandlers.DataBus;
import com.google.gson.JsonObject;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;
import dbService.DataServices.ViewDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class ViewServlet extends HttpServlet {
    private final DBService dbService;
    private final ServletUtils servletUtils;
    private final DataBus dataBus;

    public ViewServlet(
            DBService dbService,
            ServletUtils servletUtils,
            DataBus dataBus
    ) {
        this.dbService = dbService;
        this.servletUtils = servletUtils;
        this.dataBus = dataBus;
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;
        UsersDataSet usersDataSet = servletUtils.getUser(req);

        try {
            if (usersDataSet != null) {
                JsonObject body = ServletUtils.parseJsonBody(req);
                String iid = body.get("iid").getAsString();
                if (iid != null) {
                    ViewDataSet view = dbService.addView(usersDataSet, iid);
                    response = new ResponseWrapper(null, "", 200);
                    if (view != null) {
                        dataBus.broadcastAddView(view.getImage().getIid());
                    }
                } else {
                    response = new ResponseWrapper(null, "Missing iid", 400);
                }
            } else {
                response = new ResponseWrapper(null, "Not authenticated", 401);
            }
        } catch (Exception e) {
            e.printStackTrace();
            response = new ResponseWrapper(null, "Server error", 500);
        }

        servletUtils.respond(resp, response);
    }
}
