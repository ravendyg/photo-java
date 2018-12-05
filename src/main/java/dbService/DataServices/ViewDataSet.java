package dbService.DataServices;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "views")
public class ViewDataSet implements Serializable {
    @Id
    @ManyToOne
    @JoinColumn(name = "image")
    private ImageDataSet image;

    @Column(name = "date", columnDefinition = "DATETIME")
    private Date date;

    @Id
    @ManyToOne
    @JoinColumn(name = "user")
    private UsersDataSet user;

    public ImageDataSet getImage() {
        return image;
    }

    public Date getDate() {
        return date;
    }

    public UsersDataSet getUser() {
        return user;
    }

    @SuppressWarnings("UnusedDeclaration")
    public ViewDataSet() {}

    public ViewDataSet(UsersDataSet user, ImageDataSet image) {
        this.user = user;
        this.image = image;
        this.date = new Date();
    }
}
