package Helpers;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.sun.istack.internal.NotNull;
import dbService.DataServices.UsersDataSet;
import org.apache.commons.codec.binary.Hex;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class Utils {
    public static final int TEN_DAYS = 60 * 60 * 24 * 10;

    private final Algorithm algorithm;
    private final Hex hex;
    private final JWTVerifier verifier;

    public Utils(AppConfig appConfig) {
        this.algorithm = Algorithm.HMAC256(appConfig.getSecret());
        this.hex = new Hex(StandardCharsets.UTF_8);
        this.verifier = JWT.require(this.algorithm).build();
    }

    public long getRandom() {
        return Double.doubleToLongBits(Math.random());
    }

    public String getUid() {
        String uid = null;
        try {
            Long rand = getRandom();
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(rand.toString().getBytes(StandardCharsets.UTF_8));
            uid = hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }
        return uid;
    }

    @NotNull
    public String getPasswordHash(String uid, String password) {
        String passwordHash = "";
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(
                    (new StringBuilder()).append(uid).append(password).toString()
                            .getBytes(StandardCharsets.UTF_8));
            passwordHash = hex.encodeHexString(hash);
        } catch (NoSuchAlgorithmException e) {
            System.out.println(e);
        }

        return passwordHash;
    }

    // Should it be in a separate class?
    public String createGwt(UsersDataSet usersDataSet) {
        String token = JWT.create()
                .withClaim("name", usersDataSet.getName())
                .withClaim("uid", usersDataSet.getUid())
                .sign(this.algorithm);
        return token;
    }

    public String getUidFromGwt(String token) {
        try {
            DecodedJWT jwt = verifier.verify(token);
            return JWT.decode(token).getClaims().get("uid").asString();
        } catch (Exception e) {
            return null;
        }
    }
}
