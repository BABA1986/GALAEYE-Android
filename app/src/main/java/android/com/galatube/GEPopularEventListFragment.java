package android.com.galatube;

import android.com.galatube.Connectivity.GENetworkState;
import android.com.galatube.GEYoutubeEvents.GERecyclerItemClickListner;
import android.com.galatube.GEYoutubeEvents.GEPopularEventListAdapter;
import android.support.v4.app.Fragment;
import android.com.galatube.GEYoutubeEvents.GEEventListner;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.GEYoutubeEvents.GEOnLoadMore;
import android.com.galatube.GEYoutubeEvents.GEServiceManager;
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

import java.io.IOException;

/**
 * Created by deepak on 29/01/17.
 */

public class GEPopularEventListFragment extends Fragment implements GEEventListner, GEOnLoadMore,GERecyclerItemClickListner
{
    private GEServiceManager        mEvtServiceManger;
    private static RecyclerView     mSearchVideoListView;
    private int                     mPage;
    GEEventTypes                    mEventTypes;
    String                          mChannelId;
    ProgressBar                     mListProgressBar;
    private View view;
    private ImageButton mReloadPage;
    private RelativeLayout lLayout;
    private View lNoInternetView;


    // Your developer key goes here
    public static GEPopularEventListFragment newInstance(int page, GEEventTypes eventType, String channelId)
    {
        Bundle args = new Bundle();
        args.putInt(GEConstants.ARG_PAGE1, page);
        args.putString("channelid", channelId);
        args.putInt("eventtype", eventType.ordinal());
        GEPopularEventListFragment lGEVideoListFragment = new GEPopularEventListFragment();
        lGEVideoListFragment.setArguments(args);
        return lGEVideoListFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPage = getArguments().getInt(GEConstants.ARG_PAGE1);
        mChannelId = getArguments().getString("channelid");
        mEventTypes = GEEventTypes.values()[getArguments().getInt("eventtype")];
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
                        mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mSearchVideoListView = (RecyclerView)view.findViewById(R.id.recycler_view_videolist);
        mSearchVideoListView.setHasFixedSize(true);
        mSearchVideoListView
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GEPopularEventListAdapter lAdapter2 = new GEPopularEventListAdapter(getContext(), mEventTypes,  this, mChannelId,this);
        mSearchVideoListView.setAdapter(lAdapter2);// set adapter on recyclerview
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
            mSearchVideoListView.getAdapter().notifyDataSetChanged();
            mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes);
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
        mListProgressBar.setVisibility(View.INVISIBLE);
    }

    @Override
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success) {
        mSearchVideoListView.getAdapter().notifyDataSetChanged();
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
        mEvtServiceManger.loadEventsAsync(mChannelId, mEventTypes);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {

    }
}