package Helpers;

import dbService.DataServices.UsersDataSet;

public class Factories {
    private  final Utils utils;

    public Factories(Utils utils) {
        this.utils = utils;
    }

    public UsersDataSet createUser(String login, String password) {
        String uid = utils.getUid();
        String passwordHash = utils.getPasswordHash(uid, password);
        return new UsersDataSet(uid, login, passwordHash);
    }
}
