package android.com.galatube.GEPlayer;

import android.com.galatube.GEConstants;
import android.com.galatube.GETheme.GEThemeManager;
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
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ProgressBar;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by deepak on 02/12/17.
 */

public class GEPlayerActivity extends YouTubeBaseActivity implements GEVideoPlayerViewListner, GEOnLoadMore, GERecyclerItemClickListner, GEEventListner {

    private String                  mChannelID;
    private String                  mVideoId;
    private GEEventTypes            mEventTypes;
    private boolean                 mISChannelId;
    private int                     mSelectedVideoIndex;
    private GEVideoDetailView       mParallaxHeader;
    private RecyclerView            mSearchVideoListView;
    private GEServiceManager        mEvtServiceManger;
    private GEVideoPlayerView       mPlayerView;
    private boolean                 mIsPlayerFullScreen;
    private ProgressBar             mProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ge_player_activity);
        mPlayerView = (GEVideoPlayerView)findViewById(R.id.gevideoplayerview);
        mPlayerView.setListner(this);
        AdView lAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        lAdView.loadAd(adRequest);

        mSelectedVideoIndex = getIntent().getIntExtra("selectedIndex", 0);
        mISChannelId = getIntent().getBooleanExtra("ischannelId", true);
        mEventTypes = GEEventTypes.values()[getIntent().getIntExtra("eventtype", 0)];
        mChannelID = getIntent().getStringExtra("channelid");
        mVideoId = getIntent().getStringExtra("videoid");

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
        mParallaxHeader = new GEVideoDetailView(this);
        mParallaxHeader.setVideoDetailListner(new GEVideoDetailInterface() {
            @Override
            public void onShareBtnClicked() {
                shareDynamicLink();
            }
        });

        mSearchVideoListView = (RecyclerView) findViewById(R.id.videolist_onplayer);
        mSearchVideoListView.setHasFixedSize(true);
        mSearchVideoListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GEPopularEventListAdapter lAdapter2 = new GEPopularEventListAdapter(this, mEventTypes, this, mChannelID, this, GEItemType.EItemTypeList);
        lAdapter2.setScrollMultiplier((float) 0.0);
        lAdapter2.setShouldClipView(false);
        mSearchVideoListView.setAdapter(lAdapter2);// set adapter on recyclerview
        lAdapter2.setParallaxHeader(mParallaxHeader, mSearchVideoListView);

        applyTheme();
        refreshAndLoadBanner();

        try {
            mEvtServiceManger = new GEServiceManager((GEEventListner) this, this);
            mEvtServiceManger.loadEventsAsync(mChannelID, mEventTypes, mISChannelId);
            if (mVideoId != null)
                mEvtServiceManger.loadChannelAndVideoAsync(mChannelID, mVideoId, mISChannelId);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mPlayerView.playVideo();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        mPlayerView.playVideo();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
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

    private void shareDynamicLink(){
        mProgressBar.setVisibility(View.VISIBLE);

        final Video lVideo = getVideoFromSelectedIndex();
        Uri.Builder builder = new Uri.Builder();
        builder.scheme("https")
                .authority("urtube.com")
                .appendQueryParameter("channelsrc", mChannelID)
                .appendQueryParameter("videoid", lVideo.getId())
                .appendQueryParameter("ischannelid", String.valueOf(mISChannelId))
                .fragment("section-name");

        Uri lLinkURI = builder.build();

        DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLink(lLinkURI)
                .setDynamicLinkDomain("v7whn.app.goo.gl")
                // Open links with this app on Android
                .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                // Open links with com.example.ios on iOS
                .buildDynamicLink();

        Uri dynamicLinkUri = dynamicLink.getUri();


        Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
                .setLongLink(dynamicLinkUri)
                .buildShortDynamicLink()
                .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                    @Override
                    public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        if (task.isSuccessful()) {
                            // Short link created
                            Uri shortLink = task.getResult().getShortLink();
                            Uri flowchartLink = task.getResult().getPreviewLink();

                            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                            sharingIntent.setType("text/plain");
                            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, lVideo.getSnippet().getTitle() + "\n\n Stream Video on URTube App"+"\uD83D\uDC47\uD83C\uDFFD \n " + shortLink.toString());
                            startActivityForResult(sharingIntent.createChooser(sharingIntent, "Share App"), 101);
                        } else {
                            // Error
                            Log.e("TAG", "Short Dynamic link error", task.getException());

                            // ...
                        }
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // Check which request we're responding to
        mPlayerView.playVideo();
        if (requestCode == 101) {
            // Make sure the request was successful
            if (resultCode == RESULT_OK) {
            }
        }
    }

    public void refreshAndLoadBanner()
    {
        GEChannelManager lChannelMgr = GEChannelManager.getInstance();
        Channel lChannel = lChannelMgr.channelWithName(mChannelID);
        if (lChannel == null)
            return;

        if (mSelectedVideoIndex < 0)
            return;

        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventTypes, mChannelID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (mSelectedVideoIndex >= 50) ? mSelectedVideoIndex/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = mSelectedVideoIndex - lPageIndex*50;
        Video lVideo = lResults.get(lPosition);

        if (lVideo == null)
            return;

        mParallaxHeader.refreshWithInfo(lVideo, lChannel);
    }

    private void loadVideoForSelectedIndex()
    {
        if (mPlayerView == null)
            return;

        if (mSelectedVideoIndex >= 0) {
            Video lVideo = getVideoFromSelectedIndex();
            mPlayerView.loadVideo(lVideo.getId());
        }
        else{
            mPlayerView.loadVideo(mVideoId);
        }

        refreshAndLoadBanner();
    }

    public Video getVideoFromSelectedIndex(){
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventTypes, mChannelID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (mSelectedVideoIndex >= 50) ? mSelectedVideoIndex/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = mSelectedVideoIndex - lPageIndex*50;
        Video lVideo = lResults.get(lPosition);
        return lVideo;
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }

    @Override
    public void onBackPressed() {
        if (mPlayerView != null && mIsPlayerFullScreen){
            mPlayerView.setPlayerFullScreen(false);
        } else {
            super.onBackPressed();
        }
    }

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
        GEInterstitialAdMgr.showInterstitialAd();
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

    @Override
    public void dynamicLinkItemLoaded(Video video, boolean success) {
        if (video == null)
            return;
        GEChannelManager lChannelMgr = GEChannelManager.getInstance();
        Channel lChannel = lChannelMgr.channelWithName(mChannelID);
        if (lChannel == null)
            return;

        mParallaxHeader.refreshWithInfo(video, lChannel);

    }

    @Override
    public void onInitializationSuccess(GEVideoPlayerView playerView, boolean b) {
        if (!b)
            loadVideoForSelectedIndex();
    }

    @Override
    public void onInitializationFailure(GEVideoPlayerView playerView, YouTubeInitializationResult youTubeInitializationResult) {

    }

    @Override
    public void onEnterToFullScreen(boolean b){
       mIsPlayerFullScreen = b;
    }
}
