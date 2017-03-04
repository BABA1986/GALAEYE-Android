package android.com.galatube.GEPlaylist;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.PlaylistItem;

import java.util.List;

/**
 * Created by deepak on 01/03/17.
 */

public class GEVideoListPage {
    private List<PlaylistItem>          mPlayListItems;
    private String                      mNextPageToken;
    private String                      mPreviousPageToken;

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public GEVideoListPage(List<PlaylistItem> list, String nextPageToken, String PrevPageToken)
    {
        this.mPlayListItems = list;
        this.mNextPageToken = nextPageToken;
        this.mPreviousPageToken = mPreviousPageToken;
    }

    public List<PlaylistItem> getmPlayListItems() {
        return mPlayListItems;
    }
}
