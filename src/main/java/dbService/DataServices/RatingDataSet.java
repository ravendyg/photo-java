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

    public Long getValue() {
        return value;
    }

    @Column(name = "value")
    private Long value;

    @Column(name = "date")
    private long date;

    @Column(name = "image")
    private Long image;

    public RatingDataSet() {}
}
