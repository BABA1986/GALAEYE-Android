package gala.com.kidstv.GEPlaylist;

import com.google.api.services.youtube.model.PlaylistListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 01/02/17.
 */
public class GEPlaylistManager {
    private static GEPlaylistManager ourInstance = new GEPlaylistManager();
    private ArrayList<GEPlaylistObj> mPlayListObjs;

    public static GEPlaylistManager getInstance() {
        return ourInstance;
    }

    private GEPlaylistManager() {
        mPlayListObjs = new ArrayList<GEPlaylistObj>();
    }

    public ArrayList<GEPlaylistObj> getmPlayListObjs() {
        return mPlayListObjs;
    }

    public String pageTokenForInfo(String channelSource) {
        GEPlaylistObj lGEPlaylistObj = playlistListObjForInfo(channelSource);
        if (lGEPlaylistObj == null)
        {
            return null;
        }

        GEPlaylistPage lLastPage = lGEPlaylistObj.getmPlayListListPages().get(lGEPlaylistObj.getmPlayListListPages().size()-1) ;
        return lLastPage.getmNextPageToken();
    }

    public boolean canFetchMore(String channelSource)
    {
        GEPlaylistObj lGEPlaylistObj = playlistListObjForInfo(channelSource);
        if (lGEPlaylistObj == null)
        {
            return true;
        }

        GEPlaylistPage lLastPage = lGEPlaylistObj.getmPlayListListPages().get(lGEPlaylistObj.getmPlayListListPages().size()-1) ;
        if (lLastPage.getmNextPageToken() == null) {
            return false;
        }

        if (lLastPage.getmNextPageToken().length() > 0) {
            return true;
        }

        return false;
    }

    public void addPlaylistListSearchResponse(PlaylistListResponse response, String channelSource) {
        GEPlaylistObj lGEPlaylistObj = null;
        if (response.getItems().size() == 0) {
            return;
        }

        lGEPlaylistObj = playlistListObjForInfo(channelSource);

        if(lGEPlaylistObj == null) {
            lGEPlaylistObj = new GEPlaylistObj(response, channelSource);
            mPlayListObjs.add(lGEPlaylistObj);
        }
        else

        {
            lGEPlaylistObj.addPlaylistListFromResponse(response);
        }
    }

    public GEPlaylistObj playlistListObjForInfo(String channelSource)
    {
        GEPlaylistObj lListObj = null;
        GEPlaylistObj lRetListObj = null;
        for (int index = 0; index < mPlayListObjs.size(); ++index)
        {
            lListObj = mPlayListObjs.get(index);
            if (lListObj.getmChannelSource().equalsIgnoreCase(channelSource))
            {
                lRetListObj = lListObj;
                break;
            }
        }

        return lRetListObj;
    }
}
