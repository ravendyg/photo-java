package Websockets;

import Helpers.ServletUtils;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


public class WebsocketServlet extends WebSocketServlet {
    private static final int LOGOUT_TIME = 10 * 60 * 1000;
    private final WSService wsService;
    private final ServletUtils servletUtils;

    public WebsocketServlet(ServletUtils servletUtils) {
        this.wsService = new WSService();
        this.servletUtils = servletUtils;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((req, resp) -> new WSHandler(req, resp, wsService, servletUtils));
    }
}
