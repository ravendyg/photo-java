package Servlets;

import DTO.CommentDTO;
import DTO.ImageDTO;
import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.UsersDataSet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class CommentServlet extends HttpServlet {
    private final DBService dbService;
    private final ServletUtils servletUtils;

    public CommentServlet(
            DBService dbService,
            ServletUtils servletUtils
    ) {
        this.dbService = dbService;
        this.servletUtils = servletUtils;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        ResponseWrapper response;
        UsersDataSet usersDataSet = servletUtils.getUser(req);

        if (usersDataSet != null) {
            try {
                String iid = req.getPathInfo().replaceAll("\\/", "");
                List<CommentsDataSet> comments = dbService.getComments(iid);
                List<CommentDTO> commentsDTO = new ArrayList<>();
                for (CommentsDataSet comment : comments) {
                    commentsDTO.add(new CommentDTO(comment, iid));
                }
                response = new ResponseWrapper(commentsDTO, "", 200);
            } catch (Exception e) {
                e.printStackTrace();
                response = new ResponseWrapper(null, "Server error", 500);
            }
        } else {
            response = new ResponseWrapper(null, "Not authenticated", 401);
        }

        servletUtils.respond(resp, response);
    }
}
