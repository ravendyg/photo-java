package Websockets;

public interface ILongConnectionHandler {
    public void sendString(String data);

    public String getType();
}
