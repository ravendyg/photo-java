import Helpers.AppConfig;
import Helpers.Factories;
import Helpers.ServletUtils;
import Helpers.Utils;
import Servlets.*;
import Websockets.DataBus;
import Websockets.LongConnectionService;
import Websockets.LongPolingServlet;
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
    final static String USER_ROUTE = "/java/user";
    final static String IMAGE_ROUTE = "/java/photo";
    final static String VIEW_ROUTE = "/java/view";
    final static String WS_ROUTE = "/java/ws";
    final static String LP_ROUTE = "/java/lp";

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
        Utils utils = new Utils(appConfig);
        ServletUtils servletUtils = new ServletUtils(dbService, utils);
        Factories factories = new Factories(utils);

        LongConnectionService longConnectionService = new LongConnectionService();
        DataBus dataBus = new DataBus(longConnectionService);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Servlet userRouter = new UserServlet(dbService, factories, utils);
        Servlet imageProcessorServlet = new ImageServlet(dbService, servletUtils);
        Servlet viewServlet = new ViewServlet(dbService, servletUtils, dataBus);
        Servlet webSocketServlet = new WebsocketServlet(
                longConnectionService,
                servletUtils
        );
        Servlet longPolingServlet = new LongPolingServlet(
                longConnectionService,
                servletUtils
        );
        Servlet notFoundServlet = new NotFoundServlet();

        contextHandler.addServlet(new ServletHolder(userRouter), USER_ROUTE);
        contextHandler.addServlet(new ServletHolder(imageProcessorServlet), IMAGE_ROUTE);
        contextHandler.addServlet(new ServletHolder(viewServlet), VIEW_ROUTE);
        contextHandler.addServlet(new ServletHolder(webSocketServlet), WS_ROUTE);
        contextHandler.addServlet(new ServletHolder(longPolingServlet), LP_ROUTE);

        contextHandler.addServlet((new ServletHolder(notFoundServlet)), "/");

        Server server = new Server(PORT);
        server.setHandler(contextHandler);


        server.start();
        server.join();
    }
}
