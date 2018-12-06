package Websockets;

import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import com.google.gson.JsonObject;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class LPServlet extends HttpServlet {
    private final LongConnectionService longConnectionService;
    private final ServletUtils servletUtils;
    private final IAsyncProcessor processor;

    public LPServlet(
            LongConnectionService longConnectionService,
            ServletUtils servletUtils,
            IAsyncProcessor processor
    ) {
        this.longConnectionService = longConnectionService;
        this.servletUtils = servletUtils;
        this.processor = processor;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsersDataSet user = servletUtils.getUser(req);
        if (user != null) {
            new LPHandler(
                    user,
                    this.servletUtils,
                    this.longConnectionService,
                    req,
                    resp
            );
        } else {
            ResponseWrapper response = new ResponseWrapper(null, "Not authenticated", 401);
            servletUtils.respond(resp, response);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        UsersDataSet user = servletUtils.getUser(req);
        if (user != null) {
            // TODO: add ArrayBuffer handling
            JsonObject message = ServletUtils.parseJsonBody(req);
            processor.process(user, message);
        }
        // do not process 401 explicitly
        resp.setStatus(HttpServletResponse.SC_OK);
    }
}
