package DTO;

import dbService.DataServices.UsersDataSet;

import java.io.Serializable;

public class UserDTO implements Serializable {
    String name;
    String uid;

    public UserDTO(UsersDataSet usersData) {
        this.name = usersData.getName();
        this.uid = usersData.getUid();
    }
}
