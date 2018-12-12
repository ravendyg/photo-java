package DTO;

import dbService.DataServices.ImageDataSet;

import java.io.Serializable;
import java.util.Date;

public class ImagePatchDTO implements Serializable {
    final private String iid;
    final private Date changed;
    final private String description;
    final private String title;

    public ImagePatchDTO(ImageDataSet image) {
        this.iid = image.getIid();
        this.changed = image.getChanged();
        this.description = image.getDescription();
        this.title = image.getTitle();
    }
}
