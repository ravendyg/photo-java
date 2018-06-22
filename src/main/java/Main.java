import Helpers.AppConfig;
import Servlets.NotFoundServlet;
import Servlets.RootServlet;
import Servlets.ImageProcessorsServlet;
import Servlets.UserProcessor.NewUserServlet;
import Servlets.UserProcessor.SignInServlet;
import Servlets.UserProcessorServlet;
import Websockets.WebsocketServlet;
import dbService.DBService;
import org.eclipse.jetty.server.Server;
import org.eclipse.jetty.servlet.ServletContextHandler;
import org.eclipse.jetty.servlet.ServletHolder;

import javax.servlet.Servlet;
import java.io.BufferedReader;
import java.io.FileReader;

public class Main {
    final static int PORT = 4001;
    final static String ROOT_PATH = "";
    final static String USER_PROCESSOR = "/user-processor/*";
    // refactor out
    final static String NEW_USER_PATH = "/user-processor/new-user";
    // refactor out
    final static String SIGN_IN_PATH = "/user-processor/sign-in";
    final static String IMAGE_PROCESSOR = "/image-processor/*";
    final static String WEBSOCKET_ENDPOINT = "/socket";

    public static void main(String[] args) throws Exception {
        DBService dbService = new DBService();
        dbService.printConnectInfo();

        // don't catch - it's critical
        FileReader fileReader = new FileReader("config.json" );
        BufferedReader bufferedReader = new BufferedReader(fileReader);
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bufferedReader.readLine()) != null) {
            sb.append(line);
        }
        AppConfig appConfig = new AppConfig(sb.toString());


        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Servlet rootServlet = new RootServlet(dbService);
        Servlet newUser = new NewUserServlet(dbService, appConfig);
        Servlet signIn = new SignInServlet(dbService);
        Servlet notFoundServlet = new NotFoundServlet();
        Servlet userProcessorServlet = new UserProcessorServlet(dbService);
        Servlet imageProcessorServlet = new ImageProcessorsServlet(dbService, appConfig);
        Servlet webSocketServlet = new WebsocketServlet();

        contextHandler.addServlet(new ServletHolder(rootServlet), ROOT_PATH);
        contextHandler.addServlet(new ServletHolder(newUser), NEW_USER_PATH);
        contextHandler.addServlet(new ServletHolder(signIn), SIGN_IN_PATH);
        contextHandler.addServlet(new ServletHolder(userProcessorServlet), USER_PROCESSOR);
        contextHandler.addServlet(new ServletHolder(imageProcessorServlet), IMAGE_PROCESSOR);
        contextHandler.addServlet(new ServletHolder(webSocketServlet), WEBSOCKET_ENDPOINT);

        contextHandler.addServlet((new ServletHolder(notFoundServlet)), "/");

        Server server = new Server(PORT);
        server.setHandler(contextHandler);


        server.start();
        server.join();
    }
}
