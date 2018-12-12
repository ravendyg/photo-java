package DTO;

import dbService.DataServices.ViewDataSet;

import java.io.Serializable;

public class ViewDTO implements Serializable {
    private final String iid;
    private final String uid;

    public ViewDTO(ViewDataSet view) {
        this.iid = view.getImage().getIid();
        this.uid = view.getUser().getUid();
    }
}
