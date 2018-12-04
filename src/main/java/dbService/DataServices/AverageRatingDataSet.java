package dbService.DataServices;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Table;

@Entity
@Table(name = "ratings")
public class AverageRatingDataSet {
    @Column(name = "value")
    private int value;

    @Column(name = "value")
    private long count;

    public AverageRatingDataSet() {}

    public int getValue() {
        return value;
    }

    public long getCount() {
        return count;
    }
}
