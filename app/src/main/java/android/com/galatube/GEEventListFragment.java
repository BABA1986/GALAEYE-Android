package android.com.galatube;

import android.com.galatube.Connectivity.GENetworkState;
import android.com.galatube.GEYoutubeEvents.GEEventListAdapter;
import android.com.galatube.GEYoutubeEvents.GEEventListObj;
import android.com.galatube.GEYoutubeEvents.GEEventListner;
import android.com.galatube.GEYoutubeEvents.GEEventManager;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.GEYoutubeEvents.GEOnLoadMore;
import android.com.galatube.GEYoutubeEvents.GEServiceManager;
import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Toast;

/**
 * Created by deepak on 10/12/16.
 */

public class GEEventListFragment extends Fragment implements GEEventListner, GEOnLoadMore
{
    private GEServiceManager        mEvtServiceManger;
    private static RecyclerView     mLiveEventListView;
    private static RecyclerView     mUpcommingEventListView;
    private static RecyclerView     mCompletedEventListView;
    private int                     mPage;
    ProgressBar                     mProgressBar;
    private View view;

    // Your developer key goes here
    public static GEEventListFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(GEConstants.ARG_PAGE, page);
        GEEventListFragment lGEEventListFragment = new GEEventListFragment();
        lGEEventListFragment.setArguments(args);
        return lGEEventListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(GEConstants.ARG_PAGE);
        mEvtServiceManger = new GEServiceManager(this, getContext());
        mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsCompleted);
        mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsLive);
        mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsUpcomming);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        view = inflater.inflate(R.layout.event_list_fragment, container, false);

        if(!GENetworkState.isNetworkStatusAvialable(getContext()))
        {
            RelativeLayout lLayout = (RelativeLayout)view.findViewById(R.id.alleventlist);
            View lNoInternetView = inflater.inflate(R.layout.no_internet_event_fragment, container, false);
            lLayout.addView(lNoInternetView);
        }

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLiveEventListView = (RecyclerView)view.findViewById(R.id.recycler_view_live);
        mLiveEventListView.setHasFixedSize(true);
        mLiveEventListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        GEEventListAdapter lAdapter = new GEEventListAdapter(getContext(), GEEventTypes.EFetchEventsLive,  this);
        mLiveEventListView.setAdapter(lAdapter);// set adapter on recyclerview
        lAdapter.notifyDataSetChanged();// Notify the adapter

        mUpcommingEventListView = (RecyclerView)view.findViewById(R.id.recycler_view_upcomming);
        mUpcommingEventListView.setHasFixedSize(true);
        mUpcommingEventListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        GEEventListAdapter lAdapter1 = new GEEventListAdapter(getContext(), GEEventTypes.EFetchEventsUpcomming,  this);
        mUpcommingEventListView.setAdapter(lAdapter1);// set adapter on recyclerview
        lAdapter1.notifyDataSetChanged();// Notify the adapter

        mCompletedEventListView = (RecyclerView)view.findViewById(R.id.recycler_view_completed);
        mCompletedEventListView.setHasFixedSize(true);
        mCompletedEventListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        GEEventListAdapter lAdapter2 = new GEEventListAdapter(getContext(), GEEventTypes.EFetchEventsCompleted,  this);
        mCompletedEventListView.setAdapter(lAdapter2);// set adapter on recyclerview
        lAdapter2.notifyDataSetChanged();// Notify the adapter
        startLodingIndicator(view);
        refreshLayout(view);
    }

    private void startLodingIndicator(View view)
    {
        if (mProgressBar == null) {
            mProgressBar = new ProgressBar(getContext(), null, android.R.attr.progressBarStyleLarge);
            mProgressBar.setIndeterminate(true);
            FrameLayout lFrameLayout = new FrameLayout(getContext());
            lFrameLayout.setLayoutParams(new FrameLayout.LayoutParams(FrameLayout.LayoutParams.MATCH_PARENT, FrameLayout.LayoutParams.MATCH_PARENT));
            FrameLayout.LayoutParams lParams = new FrameLayout.LayoutParams(50, 50);
            lParams.gravity = Gravity.CENTER;
            ViewGroup lLayout = (ViewGroup) view;
            lFrameLayout.addView(mProgressBar, lParams);
            lLayout.addView(lFrameLayout);
            mProgressBar.setVisibility(View.VISIBLE);
        }
    }

    private void stopLodingIndicator()
    {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void refreshLayout(View fragmentView)
    {
        GEEventManager lMamager = GEEventManager.getInstance();
        LinearLayout lAllListLayout = (LinearLayout)fragmentView.findViewById(R.id.alllists);
        LinearLayout lLiveLayout = (LinearLayout)lAllListLayout.findViewById(R.id.livelistbase);

        GEEventListObj listObj = lMamager.eventListObjForInfo(GEEventTypes.EFetchEventsLive, GEConstants.GECHANNELID);
        if (listObj == null){
            lLiveLayout.setVisibility(View.GONE);
        }
        else {
            lLiveLayout.setVisibility(View.VISIBLE);
        }

        lLiveLayout = (LinearLayout)lAllListLayout.findViewById(R.id.upcomminglistbase);
        listObj = lMamager.eventListObjForInfo(GEEventTypes.EFetchEventsUpcomming, GEConstants.GECHANNELID);
        if (listObj == null){
            lLiveLayout.setVisibility(View.GONE);
        }
        else {
            lLiveLayout.setVisibility(View.VISIBLE);
        }

        lLiveLayout = (LinearLayout)lAllListLayout.findViewById(R.id.completedlistbase);
        listObj = lMamager.eventListObjForInfo(GEEventTypes.EFetchEventsCompleted, GEConstants.GECHANNELID);
        if (listObj == null){
            lLiveLayout.setVisibility(View.GONE);
        }
        else {
            lLiveLayout.setVisibility(View.VISIBLE);
        }
    }

    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success)
    {
        refreshLayout(getView());
        if (eventType == GEEventTypes.EFetchEventsCompleted) {
            mCompletedEventListView.getAdapter().notifyDataSetChanged();
        }
        else if (eventType == GEEventTypes.EFetchEventsLive) {
            mLiveEventListView.getAdapter().notifyDataSetChanged();
        }
        else if (eventType == GEEventTypes.EFetchEventsUpcomming) {
            mUpcommingEventListView.getAdapter().notifyDataSetChanged();
        }
        stopLodingIndicator();
    }

    @Override
    public void playlistsLoadedFromChannel(String channelID, boolean success) {

    }

    @Override
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success) {

    }

    @Override
    public void loadMoreItems(RecyclerView.Adapter adapter) {
        GEEventListAdapter lAdapter = (GEEventListAdapter)adapter;
        if (lAdapter.getmEventType() == GEEventTypes.EFetchEventsCompleted)
            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsCompleted);
        else if (lAdapter.getmEventType() == GEEventTypes.EFetchEventsUpcomming)
            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsUpcomming);
        else if (lAdapter.getmEventType() == GEEventTypes.EFetchEventsLive)
            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsLive);
    }
}
