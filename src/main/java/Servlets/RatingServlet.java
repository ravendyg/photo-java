package Servlets;

import DTO.RatingDTO;
import Helpers.ServletUtils;
import Websockets.DataBus;
import com.google.gson.JsonObject;
import dbService.DBService;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class RatingServlet extends HttpServlet {
    private final DBService dbService;
    private final ServletUtils servletUtils;
    private final DataBus dataBus;

    public RatingServlet(
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
        UsersDataSet user = servletUtils.getUser(req);

        String iid;
        int rating;
        JsonObject body = ServletUtils.parseJsonBody(req);
        try {
            iid = body.get("iid").getAsString();
            rating = body.get("rating").getAsInt();
            RatingDTO ratingDTO = new RatingDTO(user, iid, rating, 3, 5);
            dataBus.broadcastRating(ratingDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
