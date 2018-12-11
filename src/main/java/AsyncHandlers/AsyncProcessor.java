package AsyncHandlers;

import DTO.CommentDTO;
import DTO.DeletedCommentDTO;
import DTO.RatingDTO;
import Websockets.IAsyncProcessor;
import com.google.gson.JsonObject;
import dbService.DBException;
import dbService.DBService;
import dbService.DataServices.CommentsDataSet;
import dbService.DataServices.ImageDataSet;
import dbService.DataServices.UsersDataSet;

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
        int rating = payload.get("rating").getAsInt();
        RatingDTO ratingDTO = dbService.upsertRating(user, iid, rating);
        dataBus.broadcastRating(ratingDTO);
    }

    private void handleNewMessage(UsersDataSet user, JsonObject payload) throws  DBException {
        String iid = payload.get("iid").getAsString();
        String text = payload.get("text").getAsString();
        CommentsDataSet comment = dbService.createComment(user, iid, text);
        CommentDTO commentDTO = new CommentDTO(comment, iid);
        dataBus.broacastNewComment(commentDTO);
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
}
