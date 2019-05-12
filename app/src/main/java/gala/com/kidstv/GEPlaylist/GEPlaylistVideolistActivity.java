package gala.com.kidstv.GEPlaylist;

import gala.com.kidstv.GETheme.GEThemeManager;
import gala.com.kidstv.GEYoutubeEvents.GEEventListner;
import gala.com.kidstv.GEYoutubeEvents.GEEventTypes;
import gala.com.kidstv.GEYoutubeEvents.GEOnLoadMore;
import gala.com.kidstv.GEYoutubeEvents.GERecyclerItemClickListner;
import gala.com.kidstv.GEYoutubeEvents.GEServiceManager;
import gala.com.kidstv.R;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ProgressBar;

import com.google.api.services.youtube.model.Video;

import java.io.IOException;

/**
 * Created by deepak on 23/02/17.
 */

public class GEPlaylistVideolistActivity extends AppCompatActivity implements GEEventListner, GEOnLoadMore, GERecyclerItemClickListner
{
    GEServiceManager mServiceManager;
    String                              mPlaylistID;
    String                              mPlaylistName;
    String                              mPlaylistChannelName;
    private RecyclerView                mPlaylistVideoListView;
    ProgressBar                         mListProgressBar;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.geplaylistvideolist);
        Intent lIntent = getIntent();
        mPlaylistName = lIntent.getStringExtra("PlaylistName");
        mPlaylistID = lIntent.getStringExtra("PlaylistID");
        mPlaylistChannelName = lIntent.getStringExtra("PlaylistChannelName");

        mPlaylistVideoListView = (RecyclerView)findViewById(R.id.recycler_view_videolistid);
        mPlaylistVideoListView.setHasFixedSize(true);
        mPlaylistVideoListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GEPlaylistVideolistAdapter lAdapter2 = new GEPlaylistVideolistAdapter(this, mPlaylistID, this, this);
        mPlaylistVideoListView.setAdapter(lAdapter2); // set adapter on recyclerview
        lAdapter2.notifyDataSetChanged(); // Notify the adapter

        try {
            mServiceManager = new GEServiceManager(this, this);
        } catch (IOException e) {
            e.printStackTrace();
        }
        startLodingIndicator();
        applyTheme();
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        ActionBar lActionBar = getSupportActionBar();
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        ColorDrawable lColorDrawable = new ColorDrawable(lColor);
        lActionBar.setBackgroundDrawable(lColorDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
    }

    private void startLodingIndicator()
    {
        if (mListProgressBar == null)
        {
            mListProgressBar = new ProgressBar(this, null, android.R.attr.progressBarStyleLarge);
            mListProgressBar.setIndeterminate(true);
            FrameLayout lFrameLayout = new FrameLayout(this);
            lFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(50, 50);
            lParams.gravity = Gravity.CENTER;
            ViewGroup lLayout = (ViewGroup) this.findViewById(R.id.recycler_view_videolistactivity);
            lFrameLayout.addView(mListProgressBar, lParams);
            lLayout.addView(lFrameLayout);
            mListProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopLodingIndicator() {
        if (mListProgressBar != null)
            mListProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onYoutubeServicesAuhtenticated() {
        mServiceManager.loadPlaylistItemsAsync(mPlaylistID);
    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success)
    {

    }

    @Override
    public void playlistsLoadedFromChannel(String channelSource, boolean success)
    {

    }

    @Override
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success) {
        mPlaylistVideoListView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void dynamicLinkItemLoaded(Video video, boolean success) {

    }

    @Override
    public void loadMoreItems(RecyclerView.Adapter adapter)
    {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {

    }
}
