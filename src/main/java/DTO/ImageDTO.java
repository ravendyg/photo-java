package DTO;

import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.RatingDataSet;

import java.io.Serializable;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class ImageDTO implements Serializable {
    private String iid;
    private String extension;
    private String description;
    private String title;
    private UserDTO uploadedBy;
    private Date uploaded;
    private Date changed;
    private int commentCount;
    private String averageRating;
    private String userRating;
    private long views;

    public ImageDTO (
            ImageDataSet imageDataSet,
            List<CommentsDataSet> comments,
            HashMap<String, String> ratings
    ) {
        this.iid = imageDataSet.getIid();
        this.extension = imageDataSet.getExt();
        description = imageDataSet.getDescription();
        title = imageDataSet.getTitle();
        this.uploadedBy = new UserDTO(imageDataSet.getUploadedBy());
        uploaded = imageDataSet.getUploaded();
        changed = imageDataSet.getChanged();
        this.commentCount = comments.size();
        this.averageRating = ratings.get("average");
        this.userRating = ratings.get("user");
        this.views = imageDataSet.getViews();
    }
}
