package DTO;

import java.io.Serializable;

public class ResponseWrapper implements Serializable {
    Object payload;
    String error;
    int status;

    public ResponseWrapper(
            Object payload,
            String error,
            int status
    ) {
        this.payload = payload;
        this.error = error;
        this.status = status;
    }
}
