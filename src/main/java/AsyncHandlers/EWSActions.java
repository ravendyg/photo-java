package AsyncHandlers;

public enum EWSActions {
    NEW_PHOTO(0),
    RATING_UPDATE(1),
    PATCH_PHOTO(2),
    DELETE_PHOTO(3),
    NEW_COMMENT(4),
    DELETE_COMMENT(5),
    ADD_VIEW(6);

    private int numVal;

    EWSActions(int numVal) {
        this.numVal = numVal;
    }

    public int getNumVal() {
        return numVal;
    }
}
