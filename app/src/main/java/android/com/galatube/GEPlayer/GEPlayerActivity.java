package android.com.galatube.GEPlayer;

import android.com.galatube.GEConstants;
import android.com.galatube.GEPlaylist.GEPlaylistManager;
import android.com.galatube.GEPlaylist.GEPlaylistObj;
import android.com.galatube.GEPlaylist.GEPlaylistPage;
import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.GEYoutubeEvents.GEChannelInfoHeader;
import android.com.galatube.GEYoutubeEvents.GEChannelManager;
import android.com.galatube.GEYoutubeEvents.GEEventListner;
import android.com.galatube.GEYoutubeEvents.GEEventManager;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.GEYoutubeEvents.GEItemType;
import android.com.galatube.GEYoutubeEvents.GEOnLoadMore;
import android.com.galatube.GEYoutubeEvents.GEPopularEventListAdapter;
import android.com.galatube.GEYoutubeEvents.GERecyclerItemClickListner;
import android.com.galatube.GEYoutubeEvents.GEServiceManager;
import android.com.galatube.GEYoutubeEvents.GEVideoListObj;
import android.com.galatube.GEYoutubeEvents.GEVideoListPage;
import android.com.galatube.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by deepak on 02/12/17.
 */

public class GEPlayerActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener, GEOnLoadMore, GERecyclerItemClickListner, GEEventListner {

    private String                  mChannelID;
    private GEEventTypes            mEventTypes;
    private boolean                 mISChannelId;
    private int                     mSelectedVideoIndex;
    private GEChannelInfoHeader     mParallaxHeader;
    private RecyclerView            mSearchVideoListView;
    private GEServiceManager        mEvtServiceManger;

    private YouTubePlayer           mPlayer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ge_player_activity);

        mSelectedVideoIndex = getIntent().getIntExtra("selectedIndex", 0);
        mISChannelId = getIntent().getBooleanExtra("ischannelId", true);
        mEventTypes = GEEventTypes.values()[getIntent().getIntExtra("eventtype", 0)];
        mChannelID = getIntent().getStringExtra("channelid");

        mParallaxHeader = new GEChannelInfoHeader(this);
        mSearchVideoListView = (RecyclerView) findViewById(R.id.videolist_onplayer);
        mSearchVideoListView.setHasFixedSize(true);
        mSearchVideoListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GEPopularEventListAdapter lAdapter2 = new GEPopularEventListAdapter(this, mEventTypes, this, mChannelID, this, GEItemType.EItemTypeList);
        mSearchVideoListView.setAdapter(lAdapter2);// set adapter on recyclerview
        lAdapter2.setParallaxHeader(mParallaxHeader, mSearchVideoListView);

        YouTubePlayerView lYoutubePlayerView = (YouTubePlayerView)findViewById(R.id.youtube_view);
        lYoutubePlayerView.initialize(GEConstants.GEAPIKEY,this);
        applyTheme();
        refreshAndLoadBanner();

        try {
            mEvtServiceManger = new GEServiceManager((GEEventListner) this, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(Color.BLACK);
        }
    }

    public void refreshAndLoadBanner()
    {
        GEChannelManager lChannelMgr = GEChannelManager.getInstance();
        Channel lChannel = lChannelMgr.channelWithName(mChannelID);
        if (lChannel == null)
            return;

        BigInteger lSubscriptions = lChannel.getStatistics().getSubscriberCount();
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "in"));
        String lSubscriptionsStr = nf.format(lSubscriptions);
        String lTitle = lChannel.getSnippet().getTitle();
        String lBannerUrl = lChannel.getBrandingSettings().getImage().getBannerMobileImageUrl();
        String lThumbUrl = lChannel.getSnippet().getThumbnails().getHigh().getUrl();

        mParallaxHeader.refreshWithInfo(lBannerUrl, lThumbUrl, lTitle, lSubscriptionsStr);
    }

    private void loadVideoForSelectedIndex()
    {
        if (mPlayer == null)
            return;

        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventTypes, mChannelID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (mSelectedVideoIndex >= 50) ? mSelectedVideoIndex/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = mSelectedVideoIndex - lPageIndex*50;
        Video lVideo = lResults.get(lPosition);
        mPlayer.loadVideo(lVideo.getId());
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer = youTubePlayer;
        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!b)
            loadVideoForSelectedIndex();
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {

        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener=new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {

        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    @Override
    public void loadMoreItems(RecyclerView.Adapter adapter) {
        if (mEvtServiceManger != null)
            mEvtServiceManger.loadEventsAsync(mChannelID, mEventTypes, mISChannelId);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {
        mSelectedVideoIndex = position-1;
        loadVideoForSelectedIndex();
    }

    @Override
    public void onYoutubeServicesAuhtenticated() {

    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success) {
        refreshAndLoadBanner();
        GEPopularEventListAdapter lAdapter = (GEPopularEventListAdapter)mSearchVideoListView.getAdapter();
        lAdapter.notifyDataSetChanged();
    }

    @Override
    public void playlistsLoadedFromChannel(String channelSource, boolean success) {

    }

    @Override
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success) {

    }
}
