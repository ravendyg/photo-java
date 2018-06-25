package Websockets;

import dbService.DataServices.UsersDataSet;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.WriteCallback;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

@WebSocket
public class WSHandler {
    private WSService wsService;
    private Session session;
    private UsersDataSet user;

    public WSHandler(WSService wsService, ServletUpgradeRequest req) {
        this.wsService = wsService;
        this.user = (UsersDataSet) req.getSession().getAttribute("user");
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        wsService.add(this);
        this.session = session;
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        wsService.sendMessage(data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        wsService.remove(this);
    }

    public void sendString(String data) {
        try {
            RemoteEndpoint remote = session.getRemote();
            remote.sendString(data);
            remote.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
