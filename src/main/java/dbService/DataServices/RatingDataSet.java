package dbService.DataServices;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name = "ratings")
public class RatingDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "user")
    private Long user;

    @Column(name = "value")
    private int value;

    @Column(name = "date")
    private long date;

    @Column(name = "image")
    private Long image;

    public Long getUser() {
        return user;
    }

    public int getValue() {
        return value;
    }

    public RatingDataSet() {}
}
