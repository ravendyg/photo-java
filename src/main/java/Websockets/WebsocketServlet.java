package Websockets;

import Helpers.ServletUtils;
import org.eclipse.jetty.websocket.servlet.WebSocketServlet;
import org.eclipse.jetty.websocket.servlet.WebSocketServletFactory;

public class WebsocketServlet extends WebSocketServlet {
    private static final int LOGOUT_TIME = 10000 * 60 * 10;
    private final LongConnectionService longConnectionService;
    private final ServletUtils servletUtils;

    public WebsocketServlet(
            LongConnectionService longConnectionService,
            ServletUtils servletUtils
    ) {
        this.longConnectionService = longConnectionService;
        this.servletUtils = servletUtils;
    }

    @Override
    public void configure(WebSocketServletFactory factory) {
        factory.getPolicy().setIdleTimeout(LOGOUT_TIME);
        factory.setCreator((req, resp) -> new WSHandler(req, resp, longConnectionService, servletUtils));
    }
}
