package Websockets;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class WSService {
    private Set<WSHandler> webSockets;

    public WSService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(String data) {
        for (WSHandler socket : webSockets) {
            try {
                socket.sendString("data");
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(WSHandler webSocket) {
        System.out.println("webscoket connect");
        webSockets.add(webSocket);
    }

    public void remove(WSHandler webSocket) {
        webSockets.remove(webSocket);
    }
}
