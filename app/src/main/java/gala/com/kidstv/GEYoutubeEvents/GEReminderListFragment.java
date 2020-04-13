package gala.com.kidstv.GEYoutubeEvents;

import gala.com.kidstv.Connectivity.GENetworkState;
import gala.com.kidstv.GEConstants;
import gala.com.kidstv.R;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.recyclerview.widget.StaggeredGridLayoutManager;
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
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 07/11/17.
 */

public class GEReminderListFragment extends Fragment implements GEEventListner, GEOnLoadMore,GERecyclerItemClickListner
{
    private GEServiceManager        mEvtServiceManger;
    private static RecyclerView     mReminderVideoList;
    private int                     mPage;
    String                          mChannelId;
    ProgressBar                     mListProgressBar;
    private View                    view;
    private ImageButton             mReloadPage;
    private RelativeLayout          lLayout;
    private View                    lNoInternetView;


    // Your developer key goes here
    public static GEReminderListFragment newInstance(int page, String channelId)
    {
        Bundle args = new Bundle();
        args.putInt(GEConstants.ARG_PAGE3, page);
        args.putString("channelid", channelId);
        GEReminderListFragment lGEReminderListFragment = new GEReminderListFragment();
        lGEReminderListFragment.setArguments(args);
        return lGEReminderListFragment;
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
                            mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsReminders, true);
                    }
                }
            });
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mReminderVideoList = (RecyclerView)view.findViewById(R.id.recycler_view_videolist);
        mReminderVideoList.setHasFixedSize(true);
        mReminderVideoList
                .setLayoutManager(new StaggeredGridLayoutManager(1, StaggeredGridLayoutManager.VERTICAL));
        GEEventListAdapter lAdapter2 = new GEEventListAdapter(getContext(), GEEventTypes.EFetchEventsReminders,  this, this);
        mReminderVideoList.setAdapter(lAdapter2);// set adapter on recyclerview
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
            mReminderVideoList.getAdapter().notifyDataSetChanged();
            if (mEvtServiceManger != null)
                mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsReminders, true);
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
        mReminderVideoList.getAdapter().notifyDataSetChanged();
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
            mEvtServiceManger.loadEventsAsync(mChannelId, GEEventTypes.EFetchEventsReminders, true);
    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position) {

    }

    @Override
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType) {
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(eventType, GEConstants.GECHANNELID);
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
            if(lBellBtn.isSelected()) {
                lMamager.removeVideo(eventType, GEConstants.GECHANNELID, lPageIndex, lPosition);
                lReminderMgr.deleteReminderWinInfo(lVideoItem.getId());
                lBellBtn.setImageResource(R.drawable.notificationbell);
            }
            mReminderVideoList.getAdapter().notifyItemRemoved(position);
            lBellBtn.setSelected(!lBellBtn.isSelected());
            return;
        }
    }
}
