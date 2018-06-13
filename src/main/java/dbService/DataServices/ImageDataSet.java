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

    @ManyToOne
    @JoinColumn(name = "uploaded_by")
    private UsersDataSet uploadedBy;

    @Column(name = "src")
    private String src;

    @Column(name = "title")
    private String title;

    @Column(name = "desription")
    private String description;

    @Column(name = "uploaded")
    private long uploaded;

    @Column(name = "changed")
    private long changed;

    @ManyToOne
    @JoinColumn(name = "changed_by")
    private UsersDataSet changedBy;

    @Column(name = "views")
    private long views;

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
