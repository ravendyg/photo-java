package DTO;

import dbService.DataServices.CommentsDataSet;

import java.io.Serializable;

public class DeletedCommentDTO implements Serializable {
    private final String iid;
    private final String cid;

    public DeletedCommentDTO(CommentsDataSet comment) {
        this.iid = comment.getImage().getIid();
        this.cid = comment.getCid();
    }
}
