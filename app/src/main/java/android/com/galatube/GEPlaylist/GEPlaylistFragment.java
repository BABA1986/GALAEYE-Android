package android.com.galatube.GEPlaylist;

import android.com.galatube.GEConstants;
import android.com.galatube.GEYoutubeEvents.GEEventListner;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.GEYoutubeEvents.GEOnLoadMore;
import android.com.galatube.GEYoutubeEvents.GERecyclerItemClickListner;
import android.com.galatube.GEYoutubeEvents.GEServiceManager;
import android.com.galatube.R;
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
import android.widget.ProgressBar;

/**
 * Created by Ravi on 01/02/17.
 */

public class GEPlaylistFragment extends Fragment implements GEEventListner, GEOnLoadMore, GERecyclerItemClickListner {
    private GEServiceManager    mServiceManger;
    private RecyclerView        mPlayListListView;
    private int                 mPage;
    private String              mChannelName;
    ProgressBar                 mListProgressBar;

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
        mServiceManger = new GEServiceManager(this, getContext());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.ge_playlist_fragment, container, false);
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
        startLodingIndicator(view);
        mServiceManger.loadPlaylistAsync(mChannelName);
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
            mServiceManger.loadPlaylistAsync(mChannelName);
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
        mListProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success) {
    }

    @Override
    public void playlistsLoadedFromChannel(String channelSource, boolean success) {
        mPlayListListView.getAdapter().notifyDataSetChanged();
    }

    @Override
    public void loadMoreItems(RecyclerView.Adapter adapter) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position)
    {
        Log.d("YC", "Could not initialize:");
        Intent lIntent = new Intent(getActivity(), GEPlaylistVideolistActivity.class);
        startActivity(lIntent);
    }
}
