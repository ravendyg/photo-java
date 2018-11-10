package Helpers;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

public class AppConfig {
    private String userPhotoDirectory;
    private String secret;

    public AppConfig(String jsonStr) {
        JsonObject json = new JsonParser().parse(jsonStr).getAsJsonObject();
        this.userPhotoDirectory = json.get("userPhotoDirectory")
                .toString().replaceAll("^\"|\"$", "");
        this.secret = json.get("secret")
                .toString().replaceAll("^\"|\"$", "");
    }

    public String getUserPhotoDirectory() {
        return userPhotoDirectory;
    }

    public String getSecret() {
        return secret;
    }
}
