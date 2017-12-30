package gala.com.urtube.GEPlaylist;

import com.google.api.services.youtube.model.Playlist;

import java.util.List;

/**
 * Created by deepak on 01/02/17.
 */

public class GEPlaylistPage
{
    private List<Playlist>              mPlayListList;
    private String                      mNextPageToken;
    private String                      mPreviousPageToken;

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public GEPlaylistPage(List<Playlist> list, String nextPageToken, String PrevPageToken)
    {
        this.mPlayListList = list;
        this.mNextPageToken = nextPageToken;
        this.mPreviousPageToken = mPreviousPageToken;
    }

    public List<Playlist> getmPlaylistList() {
        return mPlayListList;
    }
}
