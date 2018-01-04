package gala.com.urtube;

import gala.com.urtube.Connectivity.GENetworkState;
import gala.com.urtube.GETheme.GEThemeManager;
import gala.com.urtube.GEYoutubeEvents.GEChannelInfoHeader;
import gala.com.urtube.GEYoutubeEvents.GEChannelManager;
import gala.com.urtube.GEYoutubeEvents.GEEventListAdapter;
import gala.com.urtube.GEYoutubeEvents.GEEventListner;
import gala.com.urtube.GEYoutubeEvents.GEEventManager;
import gala.com.urtube.GEYoutubeEvents.GEEventTypes;
import gala.com.urtube.GEYoutubeEvents.GELiveEventListAdapter;
import gala.com.urtube.GEYoutubeEvents.GEOnLoadMore;
import gala.com.urtube.GEYoutubeEvents.GERecyclerItemClickListner;
import gala.com.urtube.GEYoutubeEvents.GEReminderDataMgr;
import gala.com.urtube.GEYoutubeEvents.GEServiceManager;
import gala.com.urtube.GEYoutubeEvents.GEVideoListObj;
import gala.com.urtube.GEYoutubeEvents.GEVideoListPage;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Created by deepak on 10/12/16.
 */

public class GEEventListFragment extends Fragment implements GEEventListner, GEOnLoadMore, GERecyclerItemClickListner
{
    private GEServiceManager mEvtServiceManger;
    private static RecyclerView     mLiveEventListView;
    private static RecyclerView     mUpcommingEventListView;
    private static RecyclerView     mCompletedEventListView;
    private int                     mPage;
    ProgressBar                     mProgressBar;
    private View view;
    private ImageButton mReloadPage;
    private LinearLayout lLayout;
    private View lNoInternetView;

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
        try {
            mEvtServiceManger = new GEServiceManager(this, getContext());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.event_list_fragment, container, false);

        if(!GENetworkState.isNetworkStatusAvialable(getContext()))
        {
             lLayout = (LinearLayout)view.findViewById(R.id.alleventlist);
             lNoInternetView = inflater.inflate(R.layout.no_internet_event_fragment, container, false);
             lLayout.addView(lNoInternetView);
             mReloadPage=(ImageButton)lNoInternetView.findViewById(R.id.reload_page);
             mReloadPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if(GENetworkState.isNetworkStatusAvialable(getContext())) {

                        lLayout.removeView(lNoInternetView);
                        if (mEvtServiceManger != null) {
                            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsCompleted, true);
                            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsLive, true);
                            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsUpcomming, true);
                        }
                    }

                }
            });
        }
        else
        {
            if (mEvtServiceManger != null) {
                mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsCompleted, true);
                mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsLive, true);
                mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsUpcomming, true);
            }
        }

        refreshAndLoadBanner(view);

        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mLiveEventListView = (RecyclerView)view.findViewById(R.id.recycler_view_live);
        mLiveEventListView.setHasFixedSize(true);
        mLiveEventListView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        GELiveEventListAdapter lAdapter = new GELiveEventListAdapter(getContext(), GEEventTypes.EFetchEventsLive,  this);
        mLiveEventListView.setAdapter(lAdapter);// set adapter on recyclerview

        mUpcommingEventListView = (RecyclerView)view.findViewById(R.id.recycler_view_upcomming);
        mUpcommingEventListView.setHasFixedSize(true);
        mUpcommingEventListView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        GEEventListAdapter lAdapter1 = new GEEventListAdapter(getContext(), GEEventTypes.EFetchEventsUpcomming,  this, this);
        mUpcommingEventListView.setAdapter(lAdapter1);// set adapter on recyclerview

        mCompletedEventListView = (RecyclerView)view.findViewById(R.id.recycler_view_completed);
        mCompletedEventListView.setHasFixedSize(true);
        mCompletedEventListView.setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.HORIZONTAL));
        GEEventListAdapter lAdapter2 = new GEEventListAdapter(getContext(), GEEventTypes.EFetchEventsCompleted,  this, this);
        mCompletedEventListView.setAdapter(lAdapter2);// set adapter on recyclerview

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

    @Override
    public void onStart() {
        super.onStart();
    }

    private void stopLodingIndicator()
    {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    private void refreshLayout(View fragmentView)
    {
        if(fragmentView == null)
            return;

        GEEventManager lMamager = GEEventManager.getInstance();
        LinearLayout lAllListLayout = (LinearLayout)fragmentView.findViewById(R.id.alllists);
        RelativeLayout lParallaxLayout = (RelativeLayout)fragmentView.findViewById(R.id.paralaxbaseview);
        LinearLayout lLiveLayout = (LinearLayout)lAllListLayout.findViewById(R.id.livelistbase);
        GEVideoListObj listObj = lMamager.videoListObjForInfo(GEEventTypes.EFetchEventsLive, GEConstants.GECHANNELID);
        if (listObj != null) {
            ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
            if (listPages != null && (listPages.size() > 0)) {
                GEVideoListPage lPage = listPages.get(0);
                List<Video> lResults = lPage.getmVideoList();
                Button lMoreLiveBtn = (Button) fragmentView.findViewById(R.id.live_more_button);
                int lIsVisible = (lResults.size() < 10) ? fragmentView.GONE : fragmentView.VISIBLE;
                lMoreLiveBtn.setVisibility(lIsVisible);
                lMoreLiveBtn.setTextColor(GEThemeManager.getInstance(getContext()).getSelectedNavTextColor());
                lMoreLiveBtn.setBackgroundColor(GEThemeManager.getInstance(getContext()).getSelectedNavColor());
            }
            lLiveLayout.setVisibility(View.VISIBLE);
            lParallaxLayout.setVisibility(View.VISIBLE);
        }
        else {
            lLiveLayout.setVisibility(View.GONE);
            lParallaxLayout.setVisibility(View.GONE);
        }

        lLiveLayout = (LinearLayout)lAllListLayout.findViewById(R.id.upcomminglistbase);
        listObj = lMamager.videoListObjForInfo(GEEventTypes.EFetchEventsUpcomming, GEConstants.GECHANNELID);
        if (listObj != null) {
            ArrayList<GEVideoListPage> lUpcomPage = listObj.getmVideoListPages();
            if (lUpcomPage.size() > 0) {
                GEVideoListPage lPage = lUpcomPage.get(0);
                List<Video> lResults = lPage.getmVideoList();
                Button lMoreUpcomBtn = (Button) fragmentView.findViewById(R.id.upcomming_more_button);
                int lIsVisible = (lResults.size() < 10) ? fragmentView.GONE : fragmentView.VISIBLE;
                lMoreUpcomBtn.setVisibility(lIsVisible);
                lMoreUpcomBtn.setTextColor(GEThemeManager.getInstance(getContext()).getSelectedNavTextColor());
                lMoreUpcomBtn.setBackgroundColor(GEThemeManager.getInstance(getContext()).getSelectedNavColor());
            }
            lLiveLayout.setVisibility(View.VISIBLE);
            lParallaxLayout.setVisibility(View.VISIBLE);
        }
        else
        {
            lLiveLayout.setVisibility(View.GONE);
            lParallaxLayout.setVisibility(View.GONE);
        }

        lLiveLayout = (LinearLayout)lAllListLayout.findViewById(R.id.completedlistbase);
        listObj = lMamager.videoListObjForInfo(GEEventTypes.EFetchEventsCompleted, GEConstants.GECHANNELID);
        if (listObj != null) {
            ArrayList<GEVideoListPage> lCompPage = listObj.getmVideoListPages();
            if (lCompPage.size() > 0) {
                GEVideoListPage lPage = lCompPage.get(0);
                List<Video> lResults = lPage.getmVideoList();
                Button lMorecomBtn = (Button) fragmentView.findViewById(R.id.completed_more_button);
                int lIsVisible = (lResults.size() < 10) ? fragmentView.GONE : fragmentView.VISIBLE;
                lMorecomBtn.setVisibility(lIsVisible);
                lMorecomBtn.setTextColor(GEThemeManager.getInstance(getContext()).getSelectedNavTextColor());
                lMorecomBtn.setBackgroundColor(GEThemeManager.getInstance(getContext()).getSelectedNavColor());
            }

            lLiveLayout.setVisibility(View.VISIBLE);
            lParallaxLayout.setVisibility(View.VISIBLE);
        }
        else {
            lLiveLayout.setVisibility(View.GONE);
            lParallaxLayout.setVisibility(View.GONE);
        }
    }

    public void refreshAndLoadBanner(View view)
    {
        GEChannelManager lChannelMgr = GEChannelManager.getInstance();
        Channel lChannel = lChannelMgr.channelWithName(GEConstants.GECHANNELID);
        if (lChannel == null)
            return;

        BigInteger lSubscriptions = lChannel.getStatistics().getSubscriberCount();
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "in"));
        String lSubscriptionsStr = (lSubscriptions.equals(BigInteger.ZERO))? "" : nf.format(lSubscriptions);
        String lTitle = lChannel.getSnippet().getTitle();
        String lBannerUrl = lChannel.getBrandingSettings().getImage().getBannerMobileImageUrl();
        String lThumbUrl = lChannel.getSnippet().getThumbnails().getHigh().getUrl();

        GEChannelInfoHeader lHeader = (GEChannelInfoHeader)view.findViewById(R.id.paralaxbaseview);
        lHeader.refreshWithInfo(lBannerUrl, lThumbUrl, lTitle, lSubscriptionsStr);
    }

    @Override
    public void onYoutubeServicesAuhtenticated() {
        mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsCompleted,true);
        mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsLive, true);
        mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsUpcomming, true);
    }

    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success)
    {
        if (eventType == GEEventTypes.EFetchEventsCompleted) {
            mCompletedEventListView.getAdapter().notifyDataSetChanged();
            refreshAndLoadBanner(view);
        }
        else if (eventType == GEEventTypes.EFetchEventsLive) {
            mLiveEventListView.getAdapter().notifyDataSetChanged();
        }
        else if (eventType == GEEventTypes.EFetchEventsUpcomming) {
            mUpcommingEventListView.getAdapter().notifyDataSetChanged();
        }
        stopLodingIndicator();
        refreshLayout(getView());
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
        GEEventListAdapter lAdapter = (GEEventListAdapter)adapter;
        if (mEvtServiceManger == null)
            return;

        if (lAdapter.getmEventType() == GEEventTypes.EFetchEventsCompleted)
            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsCompleted, true);
        else if (lAdapter.getmEventType() == GEEventTypes.EFetchEventsUpcomming)
            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsUpcomming, true);
        else if (lAdapter.getmEventType() == GEEventTypes.EFetchEventsLive)
            mEvtServiceManger.loadEventsAsync(GEConstants.GECHANNELID, GEEventTypes.EFetchEventsLive, true);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventTypes)
    {
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(eventTypes, GEConstants.GECHANNELID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = position - lPageIndex*50;
        Video lVideoItem = lResults.get(lPosition);

        int lViewId = view.getId();
        if (lViewId == R.id.notificationtouchbtn)
        {
            GEReminderDataMgr lReminderMgr = GEReminderDataMgr.getInstance(getContext());
            View lCardView = (View)viewHolder.itemView;
            ImageButton lBellBtn = (ImageButton)lCardView.findViewById(R.id.notificationbtn);
            if(!lBellBtn.isSelected()) {
                lReminderMgr.addReminderWinInfo(lVideoItem.getId(), lVideoItem.getLiveStreamingDetails().getScheduledStartTime().toString());
                lBellBtn.setImageResource(R.drawable.notificationbellon);
            }
            else {
                lReminderMgr.deleteReminderWinInfo(lVideoItem.getId());
                lBellBtn.setImageResource(R.drawable.notificationbell);
            }

            lBellBtn.setSelected(!lBellBtn.isSelected());
            return;
        }
    }
}
