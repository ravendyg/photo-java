package dbService.DataServices;

import Helpers.Utils;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "comments")
public class CommentsDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cid", unique = true, updatable = false, columnDefinition = "char(64)")
    private String cid;

    @Column(name = "date", columnDefinition = "DATETIME")
    private Date date;

    @Column(name = "text")
    private String text;

    @ManyToOne
    @JoinColumn(name = "image")
    private ImageDataSet image;

    @ManyToOne
    @JoinColumn(name = "user")
    private UsersDataSet user;

    @SuppressWarnings("UnusedDeclaration")
    public CommentsDataSet() {
    }

    public CommentsDataSet(
            String cid,
            String text,
            UsersDataSet user,
            ImageDataSet image
    ) {
        this.cid = cid;
        this.date = new Date();
        this.text = text;
        this.user = user;
        this.image = image;
    }

    public long getId() {
        return id;
    }

    public String getCid() {
        return cid;
    }

    public Date getDate() {
        return date;
    }

    public String getText() {
        return text;
    }

    public ImageDataSet getImage() {
        return image;
    }

    public UsersDataSet getUser() {
        return user;
    }
}
