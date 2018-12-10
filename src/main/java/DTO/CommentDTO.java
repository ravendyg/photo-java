package DTO;

import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.UsersDataSet;

import java.io.Serializable;
import java.util.Date;

public class CommentDTO implements Serializable {
    String cid;
    String iid;
    String uid;
    Date date;
    String text;
    String userName;

    public CommentDTO(CommentsDataSet comment, String iid) {
        UsersDataSet user = comment.getUser();
        this.cid = comment.getCid();
        this.iid = iid;
        this.uid = user.getUid();
        this.date = comment.getDate();
        this.text = comment.getText();
        this.userName = user.getName();
    }
}
