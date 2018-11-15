package Websockets;

import java.io.Serializable;

enum EWSActions {
    NEW_PHOTO(1),
    RATING_UPDATE(2),
    PATCH_PHOTO(3),
    DELETE_PHOTO(4),
    NEW_COMMENT(5),
    DELET_COMMENT(6),
    ADD_VIEW(7);

    private int numVal;

    EWSActions(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}

public class LongConnectionMessage implements Serializable {
    Object payload;
    int action;

    LongConnectionMessage(Object payload, EWSActions action) {
        this.payload = payload;
        this.action = action.getNumVal();
    }
}
