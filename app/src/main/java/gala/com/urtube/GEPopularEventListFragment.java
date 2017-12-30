package gala.com.urtube;

import android.app.Activity;
import gala.com.urtube.Connectivity.GENetworkState;
import gala.com.urtube.GELaunchModal.GELaunchLinkInfo;
import gala.com.urtube.GELaunchModal.GELaunchLinkResolveListner;
import gala.com.urtube.GEPlayer.GEPlayerActivity;
import gala.com.urtube.GEYoutubeEvents.GEChannelInfoHeader;
import gala.com.urtube.GEYoutubeEvents.GEChannelManager;
import gala.com.urtube.GEYoutubeEvents.GEEventManager;
import gala.com.urtube.GEYoutubeEvents.GERecyclerItemClickListner;
import gala.com.urtube.GEYoutubeEvents.GEPopularEventListAdapter;
import gala.com.urtube.GEYoutubeEvents.GEVideoListObj;
import gala.com.urtube.GEYoutubeEvents.GEVideoListPage;
import android.content.Intent;
import android.support.v4.app.Fragment;
import gala.com.urtube.GEYoutubeEvents.GEEventListner;
import gala.com.urtube.GEYoutubeEvents.GEEventTypes;
import gala.com.urtube.GEYoutubeEvents.GEOnLoadMore;
import gala.com.urtube.GEYoutubeEvents.GEServiceManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
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

import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;

import java.io.IOException;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Locale;

/**
 * Created by deepak on 29/01/17.
 */

public class GEPopularEventListFragment extends Fragment implements GEEventListner, GEOnLoadMore,GERecyclerItemClickListner, GELaunchLinkResolveListner
{
    private GEServiceManager mEvtServiceManger;
    private RecyclerView            mSearchVideoListView;
    private int                     mPage;
    GEEventTypes                    mEventTypes;
    String                          mChannelId;
    boolean                         mISChannelId;
    ProgressBar                     mListProgressBar;
    private View view;
    private ImageButton mReloadPage;
    private RelativeLayout lLayout;
    private View lNoInternetView;
    GEChannelInfoHeader mParallaxHeader;
    private GELaunchLinkInfo mGELaunchLinkInfo;



    // Your developer key goes here
    public static GEPopularEventListFragment newInstance(int page, GEEventTypes eventType, String channelId, boolean isChannelId)
    {
        Bundle args = new Bundle();
        args.putInt(GEConstants.ARG_PAGE1, page);
        args.putString("channelid", channelId);
        args.putInt("eventtype", eventType.ordinal());
        args.putBoolean("ischannelId", isChannelId);
        GEPopularEventListFragment lGEVideoListFragment = new GEPopularEventListFragment();
        lGEVideoListFragment.setArguments(args);
        return lGEVideoListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mPage = getArguments().getInt(GEConstants.ARG_PAGE1);
        mChannelId = getArguments().getString("channelid");
        mISChannelId = getArguments().getBoolean("ischannelId");
        mEventTypes = GEEventTypes.values()[getArguments().getInt("eventtype")];
        mGELaunchLinkInfo = new GELaunchLinkInfo(this.getActivity(), this);
        mParallaxHeader = new GEChannelInfoHeader(getContext()); //(GEChannelInfoHeader)view.findViewById(R.id.paralaxbaseview);

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
                            mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes, mISChannelId);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (mSearchVideoListView == null) {
            mSearchVideoListView = (RecyclerView) view.findViewById(R.id.recycler_view_videolist);
            mSearchVideoListView.setHasFixedSize(true);
            mSearchVideoListView
                    .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
            GEPopularEventListAdapter lAdapter2 = new GEPopularEventListAdapter(getContext(), mEventTypes, this, mChannelId, this);
            mSearchVideoListView.setAdapter(lAdapter2);// set adapter on recyclerview
            lAdapter2.setParallaxHeader(mParallaxHeader, mSearchVideoListView);
        }

        mSearchVideoListView.getAdapter().notifyDataSetChanged();// Notify the adapter
        refreshAndLoadBanner(view);
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventTypes, mChannelId);
        if (listObj != null) {
            ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
            if (listPages != null)
                return;
        }

        if (mEvtServiceManger != null) {
            startLodingIndicator(getView());
            mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes, mISChannelId);
        }
    }

    @Override
    public void onStart() {
        super.onStart();

    }

    public void refreshAndLoadBanner(View view)
    {
        GEChannelManager lChannelMgr = GEChannelManager.getInstance();
        Channel lChannel = lChannelMgr.channelWithName(mChannelId);
        if (lChannel == null)
            return;

        BigInteger lSubscriptions = lChannel.getStatistics().getSubscriberCount();
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "in"));
        String lSubscriptionsStr = lSubscriptions.equals(BigInteger.ZERO) ? "" : nf.format(lSubscriptions);
        String lTitle = lChannel.getSnippet().getTitle();
        String lBannerUrl = lChannel.getBrandingSettings().getImage().getBannerMobileImageUrl();
        String lThumbUrl = lChannel.getSnippet().getThumbnails().getHigh().getUrl();

        mParallaxHeader.refreshWithInfo(lBannerUrl, lThumbUrl, lTitle, lSubscriptionsStr);
    }

    @Override
    public void setUserVisibleHint(boolean visible)
    {
        super.setUserVisibleHint(visible);
        onResume();
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
        mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes, mISChannelId);
    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success) {
        refreshAndLoadBanner(view);
         GEPopularEventListAdapter lAdapter = (GEPopularEventListAdapter)mSearchVideoListView.getAdapter();
        lAdapter.notifyDataSetChanged();

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
            mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes, mISChannelId);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {
        Intent lIntent = new Intent(getContext(), GEPlayerActivity.class);
        lIntent.putExtra("channelid", mChannelId);
        lIntent.putExtra("eventtype", eventType.ordinal());
        lIntent.putExtra("ischannelId", mISChannelId);
        lIntent.putExtra("selectedIndex", position-1);
        startActivity(lIntent);
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onResolveLaunchLink(GELaunchLinkInfo linkLaunchInfo, boolean success) {
        if (success == false)
            return;

        if (linkLaunchInfo.mVideoId == null)
            return;

        if (linkLaunchInfo.mChannelSrc == null)
            return;

        Intent lIntent = new Intent(getContext(), GEPlayerActivity.class);
        lIntent.putExtra("channelid", linkLaunchInfo.mChannelSrc);
        lIntent.putExtra("eventtype", mEventTypes);
        lIntent.putExtra("ischannelId", linkLaunchInfo.mIsChannelId);
        lIntent.putExtra("videoid", linkLaunchInfo.mVideoId);
        lIntent.putExtra("selectedIndex", -1);
        startActivity(lIntent);
    }
}