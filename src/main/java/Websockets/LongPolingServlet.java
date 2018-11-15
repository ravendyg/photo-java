package Websockets;

import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LongPolingServlet extends HttpServlet {
    private final LongConnectionService longConnectionService;
    private final ServletUtils servletUtils;

    public LongPolingServlet(
            LongConnectionService longConnectionService,
            ServletUtils servletUtils
    ) {
        this.longConnectionService = longConnectionService;
        this.servletUtils = servletUtils;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
//        UsersDataSet usersDataSet = servletUtils.getUser(req);
//        if (usersDataSet != null) {
//            new LPHandler(
//                    this.servletUtils,
//                    this.longConnectionService,
//                    resp
//            );
//        } else {
//            ResponseWrapper response = new ResponseWrapper(null, "Not authenticated", 401);
//            servletUtils.respond(resp, response);
//        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        this.longConnectionService.sendMessage("test");
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}