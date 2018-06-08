package Servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

// JSP looks like a huge headache - could not figure out how to set it up

public class RootServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        String user = "";
        resp.getWriter().println(
            "<!DOCTYPE html>" +
            "<html>" +
                "<head>" +
                    "<meta name=\"viewport\" content=\"initial-scale=1, maximum-scale=1, user-scalable=no\" />" +
                    "<link rel=\"stylesheet\" href=\"https://maxcdn.bootstrapcdn.com/font-awesome/4.5.0/css/font-awesome.min.css\">" +
                    "<link href=\"https://fonts.googleapis.com/icon?family=Material+Icons\" rel=\"stylesheet\">" +
                "</head>" +
                "<body data-user=\"" + user + "\" style=\"margin: 0;\">" +
                    "<div id=\"root\" class=\"sd\"></div>" +
                    "<script src=\"react/build/vendor.min.js\"></script>" +
                    "<script src=\"react/build/app.min.js\"></script>" +
                "</body>" +
            "</html>"
        );
    }
}