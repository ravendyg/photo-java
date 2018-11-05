package dbService.DataServices;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="sessions")
public class SessionsDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "cookie")
    private String cookie;

    @ManyToOne
    @JoinColumn(name = "user")
    private UsersDataSet user;

    public UsersDataSet getUser() {
        return this.user;
    }

    @SuppressWarnings("UnusedDeclaration")
    public SessionsDataSet() {}

    // TODO: add created time

    public SessionsDataSet(UsersDataSet user, String cookie) {
        this.user = user;
        this.cookie = cookie;
    }
}
