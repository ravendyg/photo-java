package Helpers;

public class Utils {
    public static final int TEN_DAYS = 60 * 60 * 24 * 10;

    public static String getRandom() {
        return Long.toHexString(Double.doubleToLongBits(Math.random()));
    }
}
