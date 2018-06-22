package dbService.DataServices;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "images")
public class ImageDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    public long getId() {
        return id;
    }

//    @Column(name = "uploaded_by")
    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "uploaded_by")
    private UsersDataSet uploadedBy;

    public UsersDataSet getUploadedBy() {
        return uploadedBy;
    }

    @Column(name = "src")
    private String src;

    public String getSrc() {
        return src;
    }

    @Column(name = "title")
    private String title;

    public String getTitle() {
        return title;
    }

    @Column(name = "desription")
    private String description;

    public String getDescription() {
        return description;
    }

    @Column(name = "uploaded")
    private long uploaded;

    public long getUploaded() {
        return uploaded;
    }

    @Column(name = "changed")
    private long changed;

    public long getChanged() {
        return changed;
    }

    @Column(name = "changed_by")
    private Long changedBy;

    public Long getChangedBy() {
        return changedBy;
    }

    @Column(name = "views")
    private long views;

    public long getViews() {
        return views;
    }

    public ImageDataSet() {}

    public ImageDataSet(UsersDataSet uploadedBy, String src, String title, String description) {
        this.uploadedBy = uploadedBy;
        this.src = src;
        this.title = title;
        this.description = description;
        this.uploaded = System.currentTimeMillis();
        this.changed = this.uploaded;
        this.changedBy = null;
        this.views = 0;
    }
}
