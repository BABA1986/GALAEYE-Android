package android.com.galatube.GEYoutubeEvents;

import com.google.api.services.youtube.model.SearchResult;

import java.util.List;

/**
 * Created by deepak on 04/01/17.
 */

public class GEEventListPage
{
    private List<SearchResult>          mEventList;
    private String                      mNextPageToken;
    private String                      mPreviousPageToken;

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public GEEventListPage(List<SearchResult> list, String nextPageToken, String PrevPageToken)
    {
        this.mEventList = list;
        this.mNextPageToken = nextPageToken;
        this.mPreviousPageToken = mPreviousPageToken;
    }

    public List<SearchResult> getmEventList() {
        return mEventList;
    }
}