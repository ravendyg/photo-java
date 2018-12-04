package Websockets;

import DTO.ResponseWrapper;
import Helpers.ServletUtils;
import dbService.DataServices.UsersDataSet;
import org.eclipse.jetty.continuation.Continuation;
import org.eclipse.jetty.continuation.ContinuationSupport;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class LPHandler implements ILongConnectionHandler {
    private class Runner implements Runnable {
        private String data;
        private final LPHandler lpHandler;

        public Runner(LPHandler lpHandler) {
            this.lpHandler = lpHandler;
        }

        public void send(String data) {
            this.data = data;

            synchronized (thread) {
                thread.notify();
            }
        }

        @Override
        public void run() {
            if (thread != null) {
                synchronized (thread) {
                    try {
                        thread.wait(timeout);
                        longConnectionService.remove(lpHandler);
                        ResponseWrapper response = new ResponseWrapper(data, "", 200);
                        ServletUtils.respond(resp, response);
                        continuation.resume();
                    } catch (Exception e) {
                        try {
                            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
                        } catch (Exception er) {
                            System.out.println(er);
                        }
                        System.out.println(e);
                    }
                }
            }
        }
    }

    static final String tokenKey = "Sec-WebSocket-Protocol";
    static final long timeout = 2 * 60 * 1000;

    private final ServletUtils servletUtils;
    private final LongConnectionService longConnectionService;
    private final UsersDataSet user;
    private final HttpServletResponse resp;
    private final Continuation continuation;
    private final Thread thread;
    private final Runner runner;

    public LPHandler(
            UsersDataSet user,
            ServletUtils servletUtils,
            LongConnectionService longConnectionService,
            HttpServletRequest req,
            HttpServletResponse resp
    ) {
        // TODO: does it really need that many arguments?
        this.user = user;
        this.servletUtils = servletUtils;
        this.longConnectionService = longConnectionService;
        this.resp = resp;
        this.continuation = ContinuationSupport.getContinuation(req);
        if (!this.continuation.isSuspended()) {
            this.continuation.suspend();
        }

        this.longConnectionService.add(this);

        runner = new Runner(this);
        this.thread = new Thread(runner);
        this.thread.start();
    }

    @Override
    public void sendString(String data) {
        runner.send(data);
    }

    @Override
    public String getType() {
        return "long poling";
    }
}
