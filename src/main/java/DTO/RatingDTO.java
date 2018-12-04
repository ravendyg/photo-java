package DTO;

import dbService.DataServices.UsersDataSet;

import java.io.Serializable;

public class RatingDTO implements Serializable {
    private final String uid;
    private final String iid;
    private final int value;
    private final int averageRating;
    private final long count;

    public RatingDTO(
            UsersDataSet user,
            String iid,
            int value,
            int averageRating,
            long count
    ) {
        this.uid = user.getUid();
        this.iid = iid;
        this.value = value;
        this.averageRating = averageRating;
        this.count = count;
    }
}
