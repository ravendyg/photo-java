package DTO;

import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.RatingDataSet;

import java.io.Serializable;
import java.util.Date;
import java.util.List;

public class ImageDTO implements Serializable {
    private long id;
    private String extension;
    private String description;
    private String title;
    private String uploadedBy;
    private Date uploaded;
    private Date changed;
    private int commentCount;
    private long averageRating;
    private int ratingCount;
    private int userRating;
    private long views;

    public ImageDTO (
            ImageDataSet imageDataSet,
            List<CommentsDataSet> comments,
            List<RatingDataSet> ratings,
            int userRating
    ) {
        this.id = imageDataSet.getId();
        this.extension = imageDataSet.getExt();
        description = imageDataSet.getDescription();
        title = imageDataSet.getTitle();
        this.uploadedBy = imageDataSet.getUploadedBy().getName();
        uploaded = imageDataSet.getUploaded();
        changed = imageDataSet.getChanged();
        this.commentCount = comments.size();
        long sum = 0;
        for (RatingDataSet rating : ratings) {
            sum += rating.getValue();
        }
        this.averageRating = ratings.size() > 0
                ? sum / ratings.size()
                : 0;
        this.ratingCount = ratings.size();
        this.userRating = userRating;
        this.views = imageDataSet.getViews();
    }
}
