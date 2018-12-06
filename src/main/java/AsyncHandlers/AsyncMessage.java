package AsyncHandlers;

import java.io.Serializable;

public class AsyncMessage implements Serializable {
    Object payload;
    int action;

    AsyncMessage(Object payload, EWSActions action) {
        this.payload = payload;
        this.action = action.getNumVal();
    }
}
