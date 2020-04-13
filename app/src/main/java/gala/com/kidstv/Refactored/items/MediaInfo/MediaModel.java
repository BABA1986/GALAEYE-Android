package gala.com.kidstv.Refactored.items.MediaInfo;

import com.github.vivchar.rendererrecyclerviewadapter.ViewModel;

import java.util.HashMap;

import gala.com.kidstv.Refactored.DataHandlers.emums.CategoryTypeEnum;
import gala.com.kidstv.Refactored.DataHandlers.emums.MediaTypeEnum;

public class MediaModel implements ViewModel {

    private String                                          mMediaId;
    private String                                          mMediaTitle;
    private String                                          mMediaDescription;
    private MediaTypeEnum                                   mMediaType;
    private String                                          mMediaLargeIcon;

    public MediaModel(HashMap<String, Object> mediaInfo) {
        mMediaId = (String) mediaInfo.get("mMediaId");
        mMediaTitle = (String) mediaInfo.get("mMediaTitle");
        mMediaDescription = (String) mediaInfo.get("mMediaDescription");
        mMediaLargeIcon = (String) mediaInfo.get("mMediaLargeIcon");
        Number lMediaType = (Number)mediaInfo.get("mMediaType");
        mMediaType = new MediaTypeEnum(lMediaType.intValue());
    }

    public String mediaName() {
        return mMediaTitle;
    }

    public String mediaDescription() {
        return mMediaDescription;
    }

    public String mediaTypeStr() {
        String lMediaTypeStr = "No Media Type";
        if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_VIDEO) {
            return "Video Type Media";
        }
        else if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_AUDIO) {
            return "Audio Type Media";
        }
        else if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_PHOTO) {
            return "Photo Type Media";
        }
        else if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_VIDEO_GALLERY) {
            return "Video Gallery Type Media";
        }
        else if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_AUDIO_GALLERY) {
            return "Audio Gallery Type Media";
        }
        else if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_PHOTO_GALLERY) {
            return "Photo Gallery Type Media";
        }
        else if(mMediaType.mediaType == MediaTypeEnum.EMEDIATYPE_CHANNEL) {
            return "Channel Type Media";
        }
        else {
            return lMediaTypeStr;
        }
    }
}
