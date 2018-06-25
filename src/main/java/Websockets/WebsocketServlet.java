package Websockets;

import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;


public class WebsocketServlet extends WebSocketServlet {
    private static final int LOGOUT_TIME = 10 * 60 * 1000;
    private final WSService wsService;

    public WebsocketServlet() {
        this.wsService = new WSService();
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((req, resp) -> new WSHandler(wsService, req));
    }
}
