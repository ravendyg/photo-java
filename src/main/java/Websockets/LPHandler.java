package Websockets;

import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import dbService.DataServices.UsersDataSet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LPHandler implements ILongConnectionHandler {
    static final String tokenKey = "Sec-WebSocket-Protocol";

    private ServletUtils servletUtils;
    private LongConnectionService longConnectionService;
    private UsersDataSet user;
    private HttpServletResponse resp;

    public LPHandler(
            ServletUtils servletUtils,
            LongConnectionService longConnectionService,
            HttpServletResponse resp
    ) {
        this.servletUtils = servletUtils;
        this.longConnectionService = longConnectionService;
        this.resp = resp;

        this.longConnectionService.add(this);
    }

    @Override
    public void sendString(String data) {
        System.out.println(data);
    }

    @Override
    public String getType() {
        return "long poling";
    }
}
