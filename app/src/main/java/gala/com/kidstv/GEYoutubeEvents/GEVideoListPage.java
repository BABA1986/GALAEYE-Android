package gala.com.kidstv.GEYoutubeEvents;

import com.google.api.services.youtube.model.Video;

import java.util.List;

public class GEVideoListPage
{
    private List<Video>                 mVideoList;
    private String                      mNextPageToken;
    private String                      mPreviousPageToken;

    public String getmNextPageToken() {
        return mNextPageToken;
    }

    public GEVideoListPage(List<Video> list, String nextPageToken, String PrevPageToken)
    {
        this.mVideoList = list;
        this.mNextPageToken = nextPageToken;
        this.mPreviousPageToken = mPreviousPageToken;
    }

    public List<Video> getmVideoList() {
        return mVideoList;
    }

    public void removeVideoFrom(int videoIndex){
        if (videoIndex >= mVideoList.size())
            return;

        mVideoList.remove(videoIndex);
    }
}
