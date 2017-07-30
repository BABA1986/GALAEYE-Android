package android.com.galatube.GEPlaylist;

import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 02/03/17.
 */

public class GEVideoListManager {
    private static GEVideoListManager ourInstance = new GEVideoListManager();
    private ArrayList<GEPlaylistVideolistObj> mVideoListObjs;

    public static GEVideoListManager getInstance() {
        return ourInstance;
    }

    private GEVideoListManager() {
        mVideoListObjs = new ArrayList<GEPlaylistVideolistObj>();
    }

    public ArrayList<GEPlaylistVideolistObj> getmPlayListVideoObjs() {
        return mVideoListObjs;
    }

    public String pageTokenForInfo(String sourceID) {
        GEPlaylistVideolistObj lGEPlaylistVideolistObj = playlistVideoObjForInfo(sourceID);
        if (lGEPlaylistVideolistObj == null)
        {
            return null;
        }

        GEPlaylistVideoListPage lLastPage = lGEPlaylistVideolistObj.getmVideoListPages().get(lGEPlaylistVideolistObj.getmVideoListPages().size()-1) ;
        return lLastPage.getmNextPageToken();
    }

    public boolean canFetchMore(String playlistID) {
        GEPlaylistVideolistObj lGEPlaylistVideolistObj = playlistVideoObjForInfo(playlistID);
        if (lGEPlaylistVideolistObj == null)
        {
            return true;
        }

        GEPlaylistVideoListPage lLastPage = lGEPlaylistVideolistObj.getmVideoListPages().get(lGEPlaylistVideolistObj.getmVideoListPages().size()-1) ;
        if (lLastPage.getmNextPageToken() == null) {
            return false;
        }

        if (lLastPage.getmNextPageToken().length() > 0) {
            return true;
        }

        return false;
    }

    public void addPlaylistItemSearchResponse(PlaylistItemListResponse response, String playlistID) {
        GEPlaylistVideolistObj lGEPlaylistVideolistObj = null;
        if (response.getItems().size() == 0) {
            return;
        }

        lGEPlaylistVideolistObj = playlistVideoObjForInfo(playlistID);

        if(lGEPlaylistVideolistObj == null)
        {
            lGEPlaylistVideolistObj = new GEPlaylistVideolistObj(response, playlistID);
            mVideoListObjs.add(lGEPlaylistVideolistObj);
        }
        else
        {
            lGEPlaylistVideolistObj.addPlaylistVideoFromResponse(response);
        }
    }

    public GEPlaylistVideolistObj playlistVideoObjForInfo(String playlistID)
    {
        GEPlaylistVideolistObj lListObj = null;
        GEPlaylistVideolistObj lRetListObj = null;
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
