package Websockets;

import com.google.gson.JsonObject;
import dbService.DataServices.UsersDataSet;

// TODO: UsersDataSet user - should not be here, at least explicitly
public interface IAsyncProcessor {
    public void process(UsersDataSet user, JsonObject message);

    public void process(UsersDataSet user, byte[] arrayBuffer);
}
