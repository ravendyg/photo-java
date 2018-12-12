package AsyncHandlers;

import DTO.*;
import Websockets.IAsyncProcessor;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;
import dbService.DataServices.ViewDataSet;

// TODO: where should it be?
public class AsyncProcessor implements IAsyncProcessor {
    private final DBService dbService;
    private final DataBus dataBus;

    public AsyncProcessor(DBService dbService, DataBus dataBus) {
        this.dbService = dbService;
        this.dataBus = dataBus;
    }

    @Override
    public void process(UsersDataSet user, JsonObject message) {
        try {
            EWSActions action = EWSActions.values()[message.get("action").getAsInt()];
            JsonObject payload = message.get("payload").getAsJsonObject();

            switch (action) {
                case RATING_UPDATE: {
                    handleRatingUpdate(user, payload);
                    break;
                }

                case NEW_COMMENT: {
                    handleNewMessage(user, payload);
                    break;
                }

                case DELETE_COMMENT: {
                    handleDeleteComment(user, payload);
                    break;
                }

                case DELETE_PHOTO: {
                    handleDeletePhoto(user, payload);
                    break;
                }

                case PATCH_PHOTO: {
                    handlePhotoPatch(user, payload);
                    break;
                }

                case ADD_VIEW: {
                    handleView(user, payload);
                    break;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void process(UsersDataSet user, byte[] arrayBuffer) {

    }

    private void handleRatingUpdate(UsersDataSet user, JsonObject payload) throws DBException {
        String iid = payload.get("iid").getAsString();
        Integer rating = payload.get("rating").getAsInt();
        if (iid != null && rating != null) {
            RatingDTO ratingDTO = dbService.upsertRating(user, iid, rating);
            dataBus.broadcastRating(ratingDTO);
        }
    }

    private void handleNewMessage(UsersDataSet user, JsonObject payload) throws  DBException {
        String iid = payload.get("iid").getAsString();
        String text = payload.get("text").getAsString();
        if (iid != null && text != null) {
            CommentsDataSet comment = dbService.createComment(user, iid, text);
            CommentDTO commentDTO = new CommentDTO(comment, iid);
            dataBus.broacastNewComment(commentDTO);
        }
    }

    private void handleDeleteComment(UsersDataSet user, JsonObject payload) throws DBException{
        String cid = payload.get("cid").getAsString();
        CommentsDataSet comment = dbService.deleteComment(cid, user);
        if (comment != null) {
            DeletedCommentDTO deleted = new DeletedCommentDTO(comment);
            dataBus.broadcastDeleteComment(deleted);
        }
    }

    private void handleDeletePhoto(UsersDataSet user, JsonObject payload) throws DBException {
        String iid = payload.get("iid").getAsString();
        ImageDataSet image = dbService.deletePhoto(iid, user);
        if (image != null) {
            dataBus.broadcastDeletePhoto(iid);
        }
    }

    private void handlePhotoPatch(UsersDataSet user, JsonObject payload) throws DBException {
        String iid = payload.get("iid").getAsString();
        String description = payload.get("description").getAsString();
        String title = payload.get("title").getAsString();
        if (iid != null && description != null && title != null) {
            ImageDataSet image = dbService.patchPhoto(iid, title, description);
            ImagePatchDTO imagePatchDTO = new ImagePatchDTO(image);
            dataBus.broadcastPatchPhoto(imagePatchDTO);
        }
    }

    private void handleView(UsersDataSet user, JsonObject payload) throws DBException {
        String iid = payload.get("iid").getAsString();
        if (iid != null) {
            ViewDataSet view = dbService.addView(user, iid);
            if (view != null) {
                ViewDTO viewDTO = new ViewDTO(view);
                dataBus.broadcastView(viewDTO);
            }
        }
    }
}
