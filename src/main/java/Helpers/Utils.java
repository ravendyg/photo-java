package Helpers;

import com.sun.istack.internal.NotNull;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static final int TEN_DAYS = 60 * 60 * 24 * 10;

    public static long getRandom() {
        return Double.doubleToLongBits(Math.random());
    }

    public static String getUid() {
        String uid = null;
        try {
            Long rand = getRandom();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rand.toString().getBytes(StandardCharsets.UTF_8));
            uid = hash.toString();
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return uid;
    }

    @NotNull
    public static String getPasswordHash(String uid, String password) {
        // temp hack
        return (new StringBuilder()).append(uid).append(password).toString();
    }
}
