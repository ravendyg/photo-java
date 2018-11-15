package Websockets;

import Helpers.ServletUtils;
import dbService.DataServices.UsersDataSet;
import org.eclipse.jetty.websocket.api.RemoteEndpoint;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeRequest;
import org.eclipse.jetty.websocket.servlet.ServletUpgradeResponse;

import java.util.List;

@WebSocket
public class WSHandler implements ILongConnectionHandler {
    static final String tokenKey = "Sec-WebSocket-Protocol";

    private LongConnectionService longConnectionService;
    private Session session;
    private UsersDataSet usersDataSet;

    public WSHandler(
            ServletUpgradeRequest req,
            ServletUpgradeResponse resp,
            LongConnectionService longConnectionService,
            ServletUtils servletUtils
    ) {
        this.longConnectionService = longConnectionService;
        String token = null;
        List<String> protocols = req.getSubProtocols();
        if (protocols != null && protocols.size() > 0) {
            // auth token should always be the last one
            token = protocols.get(protocols.size() - 1);
            this.usersDataSet = servletUtils.getUser(token);
        }

        if (this.usersDataSet == null) {
            try {
                resp.sendError(401, "Not authentificated");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        if (token != null) {
            resp.setHeader(WSHandler.tokenKey, token);
        }
    }

    @OnWebSocketConnect
    public void onOpen(Session session) {
        longConnectionService.add(this);
        this.session = session;
        this.ping();
    }

    @OnWebSocketMessage
    public void onMessage(String data) {
        longConnectionService.sendMessage(data);
    }

    @OnWebSocketClose
    public void onClose(int statusCode, String reason) {
        longConnectionService.remove(this);
    }

    @OnWebSocketError
    public void onError(Throwable e) {
        if (e != null) {
            e.printStackTrace();
        }
        longConnectionService.remove(this);
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

    public void ping() {
        try {
            RemoteEndpoint remote = session.getRemote();
            remote.sendPing(null);
            remote.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public String getType() {
        return "websocket";
    }
}
