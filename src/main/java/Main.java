import Servlets.RootServlet;
import Servlets.UserProcessor.NewUserServlet;
import dbService.DBService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;

public class Main {
    final static int PORT = 4001;
    final static String ROOT_PATH = "/";
    final static String NEW_USER_PATH = "/user-processor/new-user";

    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Servlet rootServlet = new RootServlet();
        Servlet newUser = new NewUserServlet(dbService);
        contextHandler.addServlet(new ServletHolder(rootServlet), ROOT_PATH);
        contextHandler.addServlet(new ServletHolder(newUser), NEW_USER_PATH);

        Server server = new Server(PORT);
        server.setHandler(contextHandler);

        server.start();
        server.join();
    }
}
