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

    @Column(name = "image")
    private Long image;

    @Column(name = "user")
    private Long user;

    public CommentsDataSet(
            String cid,
            String text,
            UsersDataSet user,
            Long image
    ) {
        this.cid = cid;
        this.date = new Date();
        this.text = text;
        this.user = user.getId();
        this.image = image;
    }
}
