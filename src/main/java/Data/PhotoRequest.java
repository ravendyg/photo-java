package Data;

import Helpers.Utils;
import dbService.DataServices.UsersDataSet;

public class PhotoRequest {
    private String iid;
    private String ext;
    private String description;
    private String title;
    private UsersDataSet uploadedBy;

    public String getIid() {
        return iid;
    }

    public String getExt() {
        return ext;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public UsersDataSet getUploadedBy() {
        return uploadedBy;
    }

    public PhotoRequest(
            String ext,
            String description,
            String title,
            UsersDataSet uploadedBy
    ) {
        this.iid = Utils.getUid();
        this.ext = ext;
        this.description = description;
        this.title = title;
        this.uploadedBy = uploadedBy;
    }
}
