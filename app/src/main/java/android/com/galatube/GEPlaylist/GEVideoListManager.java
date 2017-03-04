package android.com.galatube.GEPlaylist;

import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 02/03/17.
 */

public class GEVideoListManager {
    private static GEVideoListManager ourInstance = new GEVideoListManager();
    private ArrayList<GEVideolistObj> mVideoListObjs;

    public static GEVideoListManager getInstance() {
        return ourInstance;
    }

    private GEVideoListManager() {
        mVideoListObjs = new ArrayList<GEVideolistObj>();
    }

    public ArrayList<GEVideolistObj> getmPlayListVideoObjs() {
        return mVideoListObjs;
    }

    public String pageTokenForInfo(String sourceID) {
        GEVideolistObj lGEVideolistObj = playlistVideoObjForInfo(sourceID);
        if (lGEVideolistObj == null)
        {
            return null;
        }

        GEVideoListPage lLastPage = lGEVideolistObj.getmVideoListPages().get(lGEVideolistObj.getmVideoListPages().size()-1) ;
        return lLastPage.getmNextPageToken();
    }

    public boolean canFetchMore(String playlistID) {
        GEVideolistObj lGEVideolistObj= playlistVideoObjForInfo(playlistID);
        if (lGEVideolistObj == null)
        {
            return true;
        }

        GEVideoListPage lLastPage = lGEVideolistObj.getmVideoListPages().get(lGEVideolistObj.getmVideoListPages().size()-1) ;
        if (lLastPage.getmNextPageToken() == null) {
            return false;
        }

        if (lLastPage.getmNextPageToken().length() > 0) {
            return true;
        }

        return false;
    }

    public void addPlaylistItemSearchResponse(PlaylistItemListResponse response, String playlistID) {
        GEVideolistObj lGEVideolistObj = null;
        if (response.getItems().size() == 0) {
            return;
        }

        lGEVideolistObj = playlistVideoObjForInfo(playlistID);

        if(lGEVideolistObj == null)
        {
            lGEVideolistObj = new GEVideolistObj(response, playlistID);
            mVideoListObjs.add(lGEVideolistObj);
        }
        else
        {
            lGEVideolistObj.addPlaylistVideoFromResponse(response);
        }
    }

    public GEVideolistObj playlistVideoObjForInfo(String playlistID)
    {
        GEVideolistObj lListObj = null;
        GEVideolistObj lRetListObj = null;
        for (int index = 0; index < mVideoListObjs.size(); ++index)
        {
            lListObj = mVideoListObjs.get(index);
            if (lListObj.getmPlaylistID().equalsIgnoreCase(playlistID))
            {
                lRetListObj = lListObj;
                break;
            }
        }

        return lRetListObj;
    }
}
