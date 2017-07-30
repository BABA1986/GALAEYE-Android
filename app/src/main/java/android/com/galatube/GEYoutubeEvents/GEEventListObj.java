package android.com.galatube.GEYoutubeEvents;

import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.VideoListResponse;

import java.util.ArrayList;


/**
 * Created by deepak on 04/01/17.
 */

public class GEEventListObj
{
    private ArrayList<GEEventListPage>          mEventListPages;
    private GEEventTypes                        mEventType;
    private String                              mChannelSource;
    private int                                 mTotalResults;

    public GEEventListObj(SearchListResponse response, GEEventTypes eventTypes, String channelSource)
    {
        this.mEventType = eventTypes;
        this.mChannelSource = channelSource;
        this.mTotalResults = response.getItems().size();

        mEventListPages = new ArrayList<GEEventListPage>();
        if (response.getItems().size() == 0)
            return;

        GEEventListPage lPage = new GEEventListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mEventListPages.add(lPage);
    }

    public GEEventTypes getmEventType()
    {
        return this.mEventType;
    }

    public String getmChannelSource()
    {
        return this.mChannelSource;
    }

    public ArrayList<GEEventListPage> getmEventListPages() {
        return mEventListPages;
    }

    public void addEventFromResponse(SearchListResponse response)
    {
        GEEventListPage lPage = new GEEventListPage(response.getItems(), response.getNextPageToken(), response.getPrevPageToken());
        mEventListPages.add(lPage);
    }
}
