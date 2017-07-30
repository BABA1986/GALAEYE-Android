package android.com.galatube.GEYoutubeEvents;

import com.google.api.services.youtube.model.VideoListResponse;

import java.util.ArrayList;

/**
 * Created by deepak on 18/04/17.
 */

public class GEVideoListObj {
    private ArrayList<GEVideoListPage>          mVideoListPages;
    private GEEventTypes                        mEventType;
    private String                              mChannelSource;
    private int                                 mTotalResults;

    public GEVideoListObj(VideoListResponse response, GEEventTypes eventTypes, String channelSource)
    {
        this.mEventType = eventTypes;
        this.mChannelSource = channelSource;
        this.mTotalResults = response.getItems().size();

        mVideoListPages = new ArrayList<GEVideoListPage>();
        if (response.getItems().size() == 0)
            return;

        GEVideoListPage lPage = new GEVideoListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mVideoListPages.add(lPage);
    }

    public GEEventTypes getmEventType()
    {
        return this.mEventType;
    }

    public String getmChannelSource()
    {
        return this.mChannelSource;
    }

    public ArrayList<GEVideoListPage> getmVideoListPages() {
        return mVideoListPages;
    }

    public void addVideosFromResponse(VideoListResponse response)
    {
        GEVideoListPage lPage = new GEVideoListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mVideoListPages.add(lPage);
    }
}
