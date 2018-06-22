package dbService.DataServices;

import Helpers.Utils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "comments")
public class CommentsDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cid")
    private Long cid;

    @Column(name = "user")
    private Long user;

    @Column(name = "text")
    private String text;

    @Column(name = "date")
    private long date;

    @Column(name = "image")
    private Long image;

    public CommentsDataSet(UsersDataSet user, String text, Long image) {
        this.cid = Utils.getRandom();
        this.user = user.getId();
        this.text = text;
        this.date = System.currentTimeMillis();
        this.image = image;
    }
}
