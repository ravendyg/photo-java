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

    private final LongConnectionService longConnectionService;
    private final IAsyncProcessor asyncProcessor;

    private Session session;
    private UsersDataSet usersDataSet;

    public WSHandler(
            ServletUpgradeRequest req,
            ServletUpgradeResponse resp,
            LongConnectionService longConnectionService,
            ServletUtils servletUtils,
            IAsyncProcessor asyncProcessor
    ) {
        this.longConnectionService = longConnectionService;
        this.asyncProcessor = asyncProcessor;

        String type = null;
        List<String> protocols = req.getSubProtocols();
        if (protocols != null && protocols.size() > 0) {
            type = protocols.get(0);
            if (type != null) {
                resp.setHeader(WSHandler.tokenKey, type);
            }
        }

        String token = null;
        String queries[] = req.getRequestURI().getQuery().split("\\?");
        for (String query : queries) {
            try {
                String chunks[] = query.split("=");
                if (chunks[0].equals("token")) {
                    token = chunks[1];
                    break;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        this.usersDataSet = servletUtils.getUser(token);

        if (this.usersDataSet == null) {
            try {
                resp.sendError(401, "Not authentificated");
            } catch (Exception e) {
                e.printStackTrace();
            }
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
