package DTO;

import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.RatingDataSet;

import java.io.Serializable;
import java.util.List;

public class ImageDTO implements Serializable {
    private long id;
    private String uploadedBy;
    private String src;
    private String title;
    private String description;
    private long uploaded;
    private long changed;
    private Long changedBy;
    private long views;
    private List<CommentsDataSet> comments;
    // the naming is bad
    private List<RatingDataSet> rating;
    private Long averageRating;

    public ImageDTO (ImageDataSet imageDataSet, List<CommentsDataSet> comments, List<RatingDataSet> ratings) {
        id = imageDataSet.getId();
        uploadedBy = imageDataSet.getUploadedBy().getName();
        src = imageDataSet.getSrc();
        title = imageDataSet.getTitle();
        description = imageDataSet.getDescription();
        uploaded = imageDataSet.getUploaded();
        changed = imageDataSet.getChanged();
        changedBy = imageDataSet.getChangedBy();
        views = imageDataSet.getViews();
        this.comments = comments;
        this.rating = ratings;
        long sum = 0;
        for (RatingDataSet rating : ratings) {
            sum += rating.getValue();
        }
        this.averageRating = ratings.size() > 0
                ? sum / ratings.size()
                : 0;
    }
}
