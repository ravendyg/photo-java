package DTO;

import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;

import java.io.Serializable;
import java.util.HashMap;

public class RatingDTO implements Serializable {
    private final String uid;
    private final String iid;
    private final String value;
    private final String averageRating;

    public RatingDTO(
            UsersDataSet user,
            ImageDataSet image,
            HashMap<String, String> ratings
    ) {
        this.uid = user.getUid();
        this.iid = image.getIid();
        this.averageRating = ratings.get("average");
        this.value = ratings.get("user");
    }
}
