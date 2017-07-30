package android.com.galatube.GEPlaylist;

import android.com.galatube.GEYoutubeEvents.GEVideoListObj;

import com.google.api.services.youtube.model.PlaylistItemListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 01/03/17.
 */

public class GEPlaylistVideolistObj{
    private ArrayList<GEPlaylistVideoListPage>          mVideoListPages;
    private String                              mPlaylistID;
    private int                                 mTotalResults;

    public GEPlaylistVideolistObj(PlaylistItemListResponse response, String playlistID)
    {
        this.mPlaylistID = playlistID;
        this.mTotalResults = response.getItems().size();

        mVideoListPages = new ArrayList<GEPlaylistVideoListPage>();
        if (response.getItems().size() == 0)
            return;

        GEPlaylistVideoListPage lPage = new GEPlaylistVideoListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mVideoListPages.add(lPage);
    }

    public String getmPlaylistID()
    {
        return this.mPlaylistID;
    }

    public ArrayList<GEPlaylistVideoListPage> getmVideoListPages() {
        return mVideoListPages;
    }

    public void addPlaylistVideoFromResponse(PlaylistItemListResponse response)
    {
        GEPlaylistVideoListPage lPage = new GEPlaylistVideoListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mVideoListPages.add(lPage);
    }
}
