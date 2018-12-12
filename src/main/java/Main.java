import AsyncHandlers.DataBus;
import Helpers.AppConfig;
import Helpers.ServletUtils;
import Helpers.Utils;
import AsyncHandlers.AsyncProcessor;
import Servlets.*;
import Websockets.*;
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
    final static String RATING_ROUTE = "/java/rating";
    final static String COMMENT_ROUTE = "/java/comment/*";
    final static String IMAGE_ROUTE = "/java/photo";
    final static String WS_ROUTE = "/java/ws";
    final static String LP_ROUTE = "/java/lp";

    public static void main(String[] args) throws Exception {
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
        DBService dbService = new DBService(utils);
        dbService.printConnectInfo();
        ServletUtils servletUtils = new ServletUtils(dbService, utils);

        LongConnectionService longConnectionService = new LongConnectionService();
        DataBus dataBus = new DataBus(longConnectionService);
        IAsyncProcessor asyncProcessor = new AsyncProcessor(dbService, dataBus);

        ServletContextHandler contextHandler = new ServletContextHandler(ServletContextHandler.SESSIONS);

        Servlet userRouter = new UserServlet(dbService, utils);
        Servlet imageProcessorServlet = new ImageServlet(
                appConfig,
                dbService,
                utils,
                servletUtils,
                dataBus
        );
        Servlet ratingServlet = new RatingServlet(dbService, servletUtils, dataBus);
        Servlet commentServlet = new CommentServlet(dbService, servletUtils);
        Servlet webSocketServlet = new WebsocketServlet(
                longConnectionService,
                servletUtils,
                asyncProcessor
        );
        Servlet longPolingServlet = new LPServlet(
                longConnectionService,
                servletUtils,
                asyncProcessor
        );
        Servlet notFoundServlet = new NotFoundServlet();

        contextHandler.addServlet(new ServletHolder(userRouter), USER_ROUTE);
        contextHandler.addServlet(new ServletHolder(ratingServlet), RATING_ROUTE);
        contextHandler.addServlet(new ServletHolder(commentServlet), COMMENT_ROUTE);
        contextHandler.addServlet(new ServletHolder(imageProcessorServlet), IMAGE_ROUTE);
        contextHandler.addServlet(new ServletHolder(webSocketServlet), WS_ROUTE);
        contextHandler.addServlet(new ServletHolder(longPolingServlet), LP_ROUTE);

        contextHandler.addServlet((new ServletHolder(notFoundServlet)), "/");

        Server server = new Server(PORT);
        server.setHandler(contextHandler);


        server.start();
        server.join();
    }
}
