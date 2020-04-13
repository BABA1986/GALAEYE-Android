package gala.com.kidstv.Refactored.DataHandlers.emums;

import androidx.annotation.IntDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public class MediaTypeEnum {

    public static final int EMEDIATYPE_VIDEO = 0;
    public static final int EMEDIATYPE_AUDIO = 1;
    public static final int EMEDIATYPE_PHOTO = 2;
    public static final int EMEDIATYPE_VIDEO_GALLERY = 3;
    public static final int EMEDIATYPE_AUDIO_GALLERY = 4;
    public static final int EMEDIATYPE_PHOTO_GALLERY = 5;
    public static final int EMEDIATYPE_CHANNEL = 6;


    public @MediaTypeEnum.MediaType int      mediaType;
    public MediaTypeEnum(@MediaTypeEnum.MediaType int season) {
        mediaType = season;
        System.out.println("Season :" + season);
    }

    @IntDef({
            EMEDIATYPE_VIDEO,
            EMEDIATYPE_AUDIO,
            EMEDIATYPE_PHOTO,
            EMEDIATYPE_VIDEO_GALLERY,
            EMEDIATYPE_AUDIO_GALLERY,
            EMEDIATYPE_PHOTO_GALLERY,
            EMEDIATYPE_CHANNEL
    })
    @Retention(RetentionPolicy.SOURCE)
    public @interface MediaType {

    }
}
