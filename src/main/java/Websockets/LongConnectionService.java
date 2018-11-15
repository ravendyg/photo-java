package Websockets;

import java.util.Collections;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class LongConnectionService {
    private Set<ILongConnectionHandler> webSockets;

    public LongConnectionService() {
        this.webSockets = Collections.newSetFromMap(new ConcurrentHashMap<>());
    }

    public void sendMessage(String data) {
        for (ILongConnectionHandler connectionHandler : webSockets) {
            try {
                connectionHandler.sendString(data);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public void add(ILongConnectionHandler connectionHandler) {
        System.out.println(connectionHandler.getType() + " connected");
        webSockets.add(connectionHandler);
    }

    public void remove(ILongConnectionHandler connectionHandler) {
        webSockets.remove(connectionHandler);
    }
}
