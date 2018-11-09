package dbService.DataServices;

import Helpers.Utils;

import javax.persistence.*;
import java.io.Serializable;

@Entity
@Table(name="users")
public class UsersDataSet implements Serializable {
    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "uid", unique = true, updatable = false, columnDefinition = "char(64)")
    private String uid;

    @Column(name = "name", unique = true, updatable = false, columnDefinition = "char(64)")
    private String name;

    @Column(name = "password", columnDefinition = "char(64)")
    private String password;


    public long getId() {
        return id;
    }

    public String getUid() {
        return uid;
    }

    public String getName() {
        return name;
    }

    @SuppressWarnings("UnusedDeclaration")
    public UsersDataSet() {
    }

    public UsersDataSet(String name, String password) {
        this.name = name;
        String uid = Utils.getUid();
        this.uid = uid;
        this.password = Utils.getPasswordHash(uid, password);
    }

    public boolean matchPassword(String password) {
        return this.password.equals(password);
    }
}
