package Helpers;

public class Utils {
    public static final int TEN_DAYS = 60 * 60 * 24 * 10;

    public static long getRandom() {
        return Double.doubleToLongBits(Math.random());
    }
}
