package gala.com.urtube.GEPlaylist;

import gala.com.urtube.Connectivity.GENetworkState;
import gala.com.urtube.GEConstants;
import gala.com.urtube.GEYoutubeEvents.GEEventListner;
import gala.com.urtube.GEYoutubeEvents.GEEventTypes;
import gala.com.urtube.GEYoutubeEvents.GEOnLoadMore;
import gala.com.urtube.GEYoutubeEvents.GERecyclerItemClickListner;
import gala.com.urtube.GEYoutubeEvents.GEServiceManager;
import gala.com.urtube.R;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ravi on 01/02/17.
 */

public class GEPlaylistFragment extends Fragment implements GEEventListner, GEOnLoadMore, GERecyclerItemClickListner {
    private GEServiceManager mServiceManger;
    private RecyclerView        mPlayListListView;
    private int                 mPage;
    private String              mChannelName;
    private boolean             mIsChannelId;
    ProgressBar                 mListProgressBar;
    private View view;
    private RelativeLayout lLayout;
    private View lNoInternetView;
    private ImageButton mReloadPage;

    // Your developer key goes here
    public static GEPlaylistFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(GEConstants.ARG_PAGE2, page);
        GEPlaylistFragment lGEPlaylistFragment = new GEPlaylistFragment();
        lGEPlaylistFragment.setArguments(args);
        return lGEPlaylistFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(GEConstants.ARG_PAGE2);
        Bundle lArguments = getArguments();
        mChannelName = lArguments.getString("channelName");
        mIsChannelId = lArguments.getBoolean("ischannelId");
        try {
            mServiceManger = new GEServiceManager(this, getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
         view = inflater.inflate(R.layout.ge_playlist_fragment, container, false);
        if(!GENetworkState.isNetworkStatusAvialable(getContext())){
            lLayout = (RelativeLayout)view.findViewById(R.id.playlist);
            lNoInternetView = inflater.inflate(R.layout.no_internet_event_fragment, container, false);
            lLayout.addView(lNoInternetView);
            mReloadPage=(ImageButton)lNoInternetView.findViewById(R.id.reload_page);
            mReloadPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GENetworkState.isNetworkStatusAvialable(getContext())) {

                        lLayout.removeView(lNoInternetView);
                        mServiceManger.loadPlaylistAsync(mChannelName, mIsChannelId);
                    }
                }
            });
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mPlayListListView = (RecyclerView) view.findViewById(R.id.recycler_view_playlist);
        mPlayListListView.setHasFixedSize(true);
        mPlayListListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GEPlaylistListAdapter lAdapter2 = new GEPlaylistListAdapter(getContext(), this, this, mChannelName);
        mPlayListListView.setAdapter(lAdapter2);// set adapter on recyclerview
        lAdapter2.notifyDataSetChanged();// Notify the adapter
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void setUserVisibleHint(boolean visible) {
        super.setUserVisibleHint(visible);
        if (visible && isResumed()) {
            startLodingIndicator(getView());
            if (mServiceManger != null)
                mServiceManger.loadPlaylistAsync(mChannelName, mIsChannelId);
//            mPlayListListView.getAdapter().notifyDataSetChanged();
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    private void startLodingIndicator(View view) {
        if (mListProgressBar == null) {
            mListProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
            mListProgressBar.setIndeterminate(true);
            FrameLayout lFrameLayout = new FrameLayout(getContext());
            lFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(50, 50);
            lParams.gravity = Gravity.CENTER;
            ViewGroup lLayout = (ViewGroup) view;
            lFrameLayout.addView(mListProgressBar, lParams);
            lLayout.addView(lFrameLayout);
            mListProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopLodingIndicator() {

        if(mListProgressBar != null)
            mListProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onYoutubeServicesAuhtenticated() {
        mServiceManger.loadPlaylistAsync(mChannelName, mIsChannelId);
    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success) {
    }

    @Override
    public void playlistsLoadedFromChannel(String channelSource, boolean success) {
        mPlayListListView.getAdapter().notifyDataSetChanged();
        stopLodingIndicator();
    }

    @Override
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success) {

    }

    @Override
    public void dynamicLinkItemLoaded(Video video, boolean success) {

    }

    @Override
    public void loadMoreItems(RecyclerView.Adapter adapter) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position)
    {
        Log.d("YC", "Could not initialize:");
        GEPlaylistManager lMamager = GEPlaylistManager.getInstance();
        GEPlaylistObj listObj = lMamager.playlistListObjForInfo(mChannelName);
        ArrayList<GEPlaylistPage> listPages = listObj.getmPlayListListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEPlaylistPage lPage = listPages.get(lPageIndex);
        List<Playlist> lResults = lPage.getmPlaylistList();
        int lPosition = position - lPageIndex*50;
        Playlist lResult = lResults.get(lPosition);

        Intent lIntent = new Intent(getActivity(), GEPlaylistVideolistActivity.class);
        lIntent.putExtra("PlaylistName", lResult.getSnippet().getTitle());
        lIntent.putExtra("PlaylistChannelName", lResult.getSnippet().getChannelTitle());
        lIntent.putExtra("PlaylistID", lResult.getId());
        startActivity(lIntent);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {

    }
}
