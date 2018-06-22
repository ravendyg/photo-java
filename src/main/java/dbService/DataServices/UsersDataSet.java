package dbService.DataServices;

import javax.persistence.*;
import java.io.Serializable;

// TODO: implement correct data type with dir and hash instead of a plain text pasword
@Entity
@Table(name="users")
public class UsersDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "name", unique = true, updatable = false)
    private String name;

    // not sure why it is needed
    @Column(name = "dir", unique = true, updatable = false)
    private String dir;

    @Column(name = "password")
    private String password;

    @SuppressWarnings("UnusedDeclaration")
    public UsersDataSet() {
    }

    public UsersDataSet(String name, String password) {
        this.id = -1;
        this.name = name;
        // TODO: change to hide user name
        this.dir = name;
        this.password = password;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDir() { return dir; }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }

    @Override
    public String toString() {
        return "UserDataSet{"
                + "id=" + id
                + ", name='" + name + "'"
                + ", dir='" + dir + "'"
                + '}';

    }
}
