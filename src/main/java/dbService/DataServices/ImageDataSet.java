package dbService.DataServices;

import Data.PhotoRequest;
import Helpers.Utils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "images")
public class ImageDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "iid", unique = true, updatable = false, columnDefinition = "char(64)")
    private String iid;

    @Column(name = "ext", unique = true, updatable = false, columnDefinition = "char(5)")
    private String ext;

    @Column(name = "description")
    private String description;

    @Column(name = "title")
    private String title;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uploaded_by")
    private UsersDataSet uploadedBy;

    @Column(name = "uploaded")
    private long uploaded;

    @Column(name = "changed")
    private Long changed;

    @Column(name = "views")
    private long views;


    public long getId() {
        return id;
    }

    public String getIid() {
        return iid;
    }

    public String getExt() {
        return ext;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public UsersDataSet getUploadedBy() {
        return uploadedBy;
    }

    public long getUploaded() {
        return uploaded;
    }

    public long getChanged() {
        return changed;
    }

    public long getViews() {
        return views;
    }

    @SuppressWarnings("UnusedDeclaration")
    public ImageDataSet() {}

    public ImageDataSet(PhotoRequest request) {
        this.iid = request.getIid();
        this.ext = request.getExt();
        this.description = request.getDescription();
        this.title = request.getTitle();
        this.uploadedBy = request.getUploadedBy();
        this.uploaded = System.currentTimeMillis();
        this.changed = null;
        this.views = 0;
    }
}
