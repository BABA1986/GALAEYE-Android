package android.com.galatube.GEYoutubeEvents;

import android.com.galatube.GEPlaylist.GEPlaylistVideolistObj;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

import java.util.ArrayList;

/*****
 * Created by deepak on 04/01/17.
 *****/

public class GEEventManager {
    private static GEEventManager ourInstance = new GEEventManager();

    public static GEEventManager getInstance() {
        return ourInstance;
    }

    private ArrayList<GEVideoListObj> mVideoListObjs;

    private GEEventManager() {
        mVideoListObjs = new ArrayList<GEVideoListObj>();
    }


    public ArrayList<GEVideoListObj> getmVideoListObjs() {
        return mVideoListObjs;
    }

    public String pageTokenForInfo(GEEventTypes eventType, String channelID)
    {
        GEVideoListObj lGEVideoListObj = videoListObjForInfo(eventType, channelID);
        if (lGEVideoListObj == null)
        {
            return null;
        }

        GEVideoListPage lLastPage = lGEVideoListObj.getmVideoListPages().get(lGEVideoListObj.getmVideoListPages().size()-1) ;
        return lLastPage.getmNextPageToken();
    }

    public boolean canFetchMore(GEEventTypes eventType, String channelID)
    {
        GEVideoListObj lGEVideoListObj = videoListObjForInfo(eventType, channelID);
        if (lGEVideoListObj == null)
        {
            return true;
        }

        GEVideoListPage lLastPage = lGEVideoListObj.getmVideoListPages().get(lGEVideoListObj.getmVideoListPages().size()-1) ;

        if (lLastPage.getmNextPageToken() == null){
            return false;
        }

        if (lLastPage.getmNextPageToken().length() > 0) {
            return true;
        }

        return false;
    }

    public void removeVideo(GEEventTypes eventType, String channelID, int pageIndex, int videoIndex)
    {
        int index = 0;
        for (; index < mVideoListObjs.size(); ++index)
        {
            GEVideoListObj lListObj = mVideoListObjs.get(index);
            if ((eventType == lListObj.getmEventType()) && (lListObj.getmChannelSource().equalsIgnoreCase(channelID)))
            {
                lListObj.removeVideoFrom(pageIndex, videoIndex);
                break;
            }
        }
    }

    public void addVideoSearchResponse(VideoListResponse response, GEEventTypes eventType, String channelID)
    {
        GEVideoListObj lGEVideoListObj = null;
        if (response.getItems().size() == 0) {
            return;
        }

        lGEVideoListObj = videoListObjForInfo(eventType, channelID);

        if(lGEVideoListObj == null)
        {
            lGEVideoListObj = new GEVideoListObj(response, eventType, channelID);
            mVideoListObjs.add(lGEVideoListObj);
        }
        else
        {
            lGEVideoListObj.addVideosFromResponse(response);
        }
    }


    public GEVideoListObj videoListObjForInfo(GEEventTypes eventType, String channelID)
    {
        GEVideoListObj lListObj = null;
        GEVideoListObj lRetListObj = null;
        for (int index = 0; index < mVideoListObjs.size(); ++index)
        {
            lListObj = mVideoListObjs.get(index);
            if ((eventType == lListObj.getmEventType()) && (lListObj.getmChannelSource().equalsIgnoreCase(channelID)))
            {
                lRetListObj = lListObj;
                break;
            }
        }

        return lRetListObj;
    }
}