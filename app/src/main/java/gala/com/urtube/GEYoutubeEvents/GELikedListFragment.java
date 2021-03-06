package gala.com.urtube.GEYoutubeEvents;

import gala.com.urtube.Connectivity.GENetworkState;
import gala.com.urtube.GEConstants;
import gala.com.urtube.R;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.api.services.youtube.model.Video;

import java.io.IOException;

/**
 * Created by deepak on 18/04/17.
 */

public class GELikedListFragment extends Fragment implements GEEventListner, GEOnLoadMore,GERecyclerItemClickListner
{
    private GEServiceManager        mEvtServiceManger;
    private static RecyclerView     mLikedVideoList;
    private int                     mPage;
    String                          mChannelId;
    ProgressBar                     mListProgressBar;
    private View                    view;
    private ImageButton             mReloadPage;
    private RelativeLayout          lLayout;
    private View                    lNoInternetView;


    // Your developer key goes here
    public static GELikedListFragment newInstance(int page, String channelId)
    {
        Bundle args = new Bundle();
        args.putInt(GEConstants.ARG_PAGE3, page);
        args.putString("channelid", channelId);
        GELikedListFragment lGELikedListFragment = new GELikedListFragment();
        lGELikedListFragment.setArguments(args);
        return lGELikedListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(GEConstants.ARG_PAGE3);
        mChannelId = getArguments().getString("channelid");
        try {
            mEvtServiceManger = new GEServiceManager(this, getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.ge_videolist_fragment, container, false);
        if(!GENetworkState.isNetworkStatusAvialable(getContext()))
        {
            lLayout = (RelativeLayout)view.findViewById(R.id.AllVideoList);
            lNoInternetView = inflater.inflate(R.layout.no_internet_event_fragment, container, false);
            mReloadPage=(ImageButton)lNoInternetView.findViewById(R.id.reload_page);
            lLayout.addView(lNoInternetView);
            mReloadPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GENetworkState.isNetworkStatusAvialable(getContext())) {

                        lLayout.removeView(lNoInternetView);
                        if (mEvtServiceManger != null)
                            mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsLiked, true);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mLikedVideoList = (RecyclerView)view.findViewById(R.id.recycler_view_videolist);
        mLikedVideoList.setHasFixedSize(true);
        mLikedVideoList
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GELikedListAdapter lAdapter2 = new GELikedListAdapter(getContext(), GEEventTypes.EFetchEventsLiked,  this, mChannelId,this);
        mLikedVideoList.setAdapter(lAdapter2);// set adapter on recyclerview
        lAdapter2.notifyDataSetChanged();// Notify the adapter
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        if (visible && isResumed())
        {
            startLodingIndicator(getView());
            mLikedVideoList.getAdapter().notifyDataSetChanged();
            if (mEvtServiceManger != null)
                mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsLiked, true);
            //Only manually call onResume if fragment is already visible
            //Otherwise allow natural fragment lifecycle to call onResume
            onResume();
        }
    }

    private void startLodingIndicator(View view)
    {
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

    private void stopLodingIndicator()
    {
        if (mListProgressBar != null)
            mListProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onYoutubeServicesAuhtenticated() {
        mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsLiked, true);
    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success) {
        mLikedVideoList.getAdapter().notifyDataSetChanged();
        stopLodingIndicator();
    }

    @Override
    public void playlistsLoadedFromChannel(String channelID, boolean success) {

    }

    @Override
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success) {

    }

    @Override
    public void dynamicLinkItemLoaded(Video video, boolean success) {

    }

    @Override
    public void loadMoreItems(RecyclerView.Adapter adapter) {
        if (mEvtServiceManger != null)
            mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsLiked, true);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {

    }
}
