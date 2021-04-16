package gala.com.kidstv.Refactored.RoutersAndPresenters;

import android.app.Activity;
import android.content.Intent;

import gala.com.kidstv.GEPlayer.GEPlayerActivity;
import gala.com.kidstv.Refactored.DataHandlers.emums.MediaTypeEnum;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;

public class ActionPerformer {

    public static void route(MediaModel model, Activity activity){
        MediaTypeEnum screenType = model.getMediaType();
        switch (screenType.mediaType){
            case MediaTypeEnum.EMEDIATYPE_PHOTO:
                break;
            case MediaTypeEnum.EMEDIATYPE_AUDIO:
                break;
            case MediaTypeEnum.EMEDIATYPE_VIDEO:
                openGEPlayer(model, activity);
                break;
            case MediaTypeEnum.EMEDIATYPE_PHOTO_GALLERY:
                break;
            case MediaTypeEnum.EMEDIATYPE_AUDIO_GALLERY:
                break;
            case MediaTypeEnum.EMEDIATYPE_VIDEO_GALLERY:
                break;
            case MediaTypeEnum.EMEDIATYPE_CHANNEL:
                break;
            default:
                break;

        }
    }

    private static void openGEPlayer(MediaModel model, Activity activity) {
        Intent intent = new Intent(activity, GEPlayerActivity.class);
        activity.startActivity(intent);
    }
}
