package AsyncHandlers;

import DTO.ImageDTO;
import DTO.RatingDTO;
import Websockets.LongConnectionService;
import com.google.gson.GsonBuilder;

public class DataBus {
    private  final LongConnectionService longConnectionService;

    public DataBus(LongConnectionService longConnectionService) {
        this.longConnectionService = longConnectionService;
    }

    public void broadcastAddView(String iid) {
        String message = prepareMessage(iid, EWSActions.ADD_VIEW);
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

    private String prepareMessage(Object payload, EWSActions action) {
        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.serializeNulls();
        String json = gsonBuilder.create().toJson(new AsyncMessage(
                payload, action
        ));
        return json;
    }
}
