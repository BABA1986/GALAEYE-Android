package android.com.galatube.GEPlaylist;

import android.com.galatube.GEYoutubeEvents.GEEventListPage;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;

import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.SearchListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 01/02/17.
 */

public class GEPlaylistObj
{
    private ArrayList<GEPlaylistPage>           mPlayListListPages;
    private String                              mChannelSource;
    private int                                 mTotalResults;

    public GEPlaylistObj(PlaylistListResponse response, String channelSource)
    {
        this.mChannelSource = channelSource;
        this.mTotalResults = response.getItems().size();

        mPlayListListPages = new ArrayList<GEPlaylistPage>();
        if (response.getItems().size() == 0)
            return;

        GEPlaylistPage lPage = new GEPlaylistPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mPlayListListPages.add(lPage);
    }

    public String getmChannelSource()
    {
        return this.mChannelSource;
    }

    public ArrayList<GEPlaylistPage> getmPlayListListPages() {
        return mPlayListListPages;
    }

    public void addPlaylistListFromResponse(PlaylistListResponse response)
    {
        GEPlaylistPage lPage = new GEPlaylistPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mPlayListListPages.add(lPage);
    }
}

