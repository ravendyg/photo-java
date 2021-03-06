package AsyncHandlers;

import DTO.*;
import Websockets.LongConnectionService;
import com.google.gson.GsonBuilder;

public class DataBus {
    private  final LongConnectionService longConnectionService;

    public DataBus(LongConnectionService longConnectionService) {
        this.longConnectionService = longConnectionService;
    }

    public void broadcastView(ViewDTO viewDTO) {
        String message = prepareMessage(viewDTO, EWSActions.ADD_VIEW);
        this.longConnectionService.sendMessage(message);
    }

    public void broadcastRating(RatingDTO ratingDTO) {
        String message = prepareMessage(ratingDTO, EWSActions.RATING_UPDATE);
        this.longConnectionService.sendMessage(message);
    }

    public void broadcastDeletePhoto(String iid) {
        String message = prepareMessage(iid, EWSActions.DELETE_PHOTO);
        this.longConnectionService.sendMessage(message);
    }

    public void broadcastNewPhoto(ImageDTO imageDTO) {
        String message = prepareMessage(imageDTO, EWSActions.NEW_PHOTO);
        this.longConnectionService.sendMessage(message);
    }

    public void broacastNewComment(CommentDTO commentDTO) {
        String message = prepareMessage(commentDTO, EWSActions.NEW_COMMENT);
        this.longConnectionService.sendMessage(message);
    }

    public void broadcastDeleteComment(DeletedCommentDTO deleted) {
        String message = prepareMessage(deleted, EWSActions.DELETE_COMMENT);
        this.longConnectionService.sendMessage(message);
    }

    public void broadcastPatchPhoto(ImagePatchDTO imagePatchDTO) {
        String message = prepareMessage(imagePatchDTO, EWSActions.PATCH_PHOTO);
        this.longConnectionService.sendMessage(message);
    }

    private String prepareMessage(Object payload, EWSActions action) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        String json = gsonBuilder.create().toJson(new AsyncMessage(
                payload, action
        ));
        return json;
    }
}
