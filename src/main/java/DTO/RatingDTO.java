package DTO;

import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;

import java.io.Serializable;
import java.util.HashMap;

public class RatingDTO implements Serializable {
    private final String uid;
    private final String iid;
    private final int value;
    private final double averageRating;
    private final long count;

    public RatingDTO(
            UsersDataSet user,
            ImageDataSet image,
            HashMap<String, Number> ratings
    ) {
        this.uid = user.getUid();
        this.iid = image.getIid();
        this.averageRating = (double) ratings.get("average");
        this.count = (long) ratings.get("count");
        this.value = (int) ratings.get("user");
    }
}
