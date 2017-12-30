package gala.com.urtube.GEPlaylist;

import com.google.api.services.youtube.model.PlaylistItem;

import java.util.List;

/**
 * Created by deepak on 01/03/17.
 */

public class GEPlaylistVideoListPage {
    private List<PlaylistItem>          mPlayListItems;
    private String                      mNextPageToken;
    private String                      mPreviousPageToken;

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public GEPlaylistVideoListPage(List<PlaylistItem> list, String nextPageToken, String PrevPageToken)
    {
        this.mPlayListItems = list;
        this.mNextPageToken = nextPageToken;
        this.mPreviousPageToken = mPreviousPageToken;
    }

    public List<PlaylistItem> getmPlayListItems() {
        return mPlayListItems;
    }
}
