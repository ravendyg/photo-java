package dbService.DataServices;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "ratings")
public class RatingDataSet implements Serializable {
    @Id
    @Column(name = "user")
    private Long user;

    @Column(name = "value")
    private int value;

    @Column(name = "date", columnDefinition = "DATETIME")
    private Date date;

    @Id
    @Column(name = "image")
    private long image;

    public int getValue() {
        return value;
    }

    public RatingDataSet() {}
}
