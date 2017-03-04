package android.com.galatube.GEPlaylist;

import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 01/03/17.
 */

public class GEVideolistObj {
    private ArrayList<GEVideoListPage>          mVideoListPages;
    private String                              mPlaylistID;
    private int                                 mTotalResults;

    public GEVideolistObj(PlaylistItemListResponse response, String playlistID)
    {
        this.mPlaylistID = playlistID;
        this.mTotalResults = response.getItems().size();

        mVideoListPages = new ArrayList<GEVideoListPage>();
        if (response.getItems().size() == 0)
            return;

        GEVideoListPage lPage = new GEVideoListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mVideoListPages.add(lPage);
    }

    public String getmPlaylistID()
    {
        return this.mPlaylistID;
    }

    public ArrayList<GEVideoListPage> getmVideoListPages() {
        return mVideoListPages;
    }

    public void addPlaylistVideoFromResponse(PlaylistItemListResponse response)
    {
        GEVideoListPage lPage = new GEVideoListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mVideoListPages.add(lPage);
    }
}
