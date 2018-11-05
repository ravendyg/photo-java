package DTO;

import java.io.Serializable;

public class ResponseWrapper<T> implements Serializable {
    T payload;
    String error;
    int status;

    public ResponseWrapper(
            T payload,
            String error,
            int status
    ) {
        this.payload = payload;
        this.error = error;
        this.status = status;
    }
}
