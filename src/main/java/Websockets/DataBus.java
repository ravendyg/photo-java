package Websockets;

import com.google.gson.GsonBuilder;

public class DataBus {
    private  final LongConnectionService longConnectionService;

    public DataBus(LongConnectionService longConnectionService) {
        this.longConnectionService = longConnectionService;
    }

    public void broadcastAddView(String iid) {
        String message = prepareMessage(iid, EWSActions.ADD_VIEW);
        this.longConnectionService.sendMessage(message);
    }

    private String prepareMessage(Object payload, EWSActions action) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        String json = gsonBuilder.create().toJson(new LongConnectionMessage(
                payload, action
        ));
        return json;
    }
}
