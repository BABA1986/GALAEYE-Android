package gala.com.kidstv.GEYoutubeEvents;

import gala.com.kidstv.GEConstants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import androidx.recyclerview.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by deepak on 16/01/17.
 */

public class GEEventListAdapter extends
        RecyclerView.Adapter<GEEventListItemView> {

    private Context         mContext;
    private GEOnLoadMore    mLoadMoreListner;
    private GEEventTypes    mEventType;
    private GERecyclerItemClickListner  mClickListner;

    public GEEventListAdapter(Context context, GEEventTypes eventType, GEOnLoadMore loadmoreListner, GERecyclerItemClickListner clickListner) {
        this.mContext = context;
        this.mLoadMoreListner = loadmoreListner;
        this.mEventType = eventType;
        this.mClickListner = clickListner;
    }

    public GEEventTypes getmEventType() {
        return mEventType;
    }

    @Override
    public int getItemCount() {
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, GEConstants.GECHANNELID);
        if (listObj == null) return 0;
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        GEVideoListPage lPage = listPages.get(listPages.size() - 1);
        List<Video> lResults = lPage.getmVideoList();
        if (lResults.size()>10){
            return 10;
        }else {
            return lResults.size();
        }
    }

    @Override
    public void onBindViewHolder(GEEventListItemView holder, int position) {

        GEEventListItemView lListItem = (GEEventListItemView) holder;// holder
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, GEConstants.GECHANNELID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = position - lPageIndex*50;
        Video lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());
        lListItem.mNotificationBtn.setVisibility(View.GONE);
        String lDateTimeStr = lResult.getLiveStreamingDetails().getScheduledStartTime().toString();
        lDateTimeStr = fomateDateStr(lDateTimeStr);

        if (mEventType == GEEventTypes.EFetchEventsUpcomming
                || mEventType == GEEventTypes.EFetchEventsReminders) {
            lListItem.mNotificationBtn.setVisibility(View.VISIBLE);
            lDateTimeStr = "Live on " + lDateTimeStr;
            boolean lIsReminder = GEReminderDataMgr.getInstance(mContext).isVideoReminderWithId(lResult.getId());
            lListItem.mNotificationBtn.setSelected(lIsReminder);
            if(lIsReminder)
                lListItem.mNotificationBtn.setImageResource(gala.com.kidstv.R.drawable.notificationbellon);
            else
                lListItem.mNotificationBtn.setImageResource(gala.com.kidstv.R.drawable.notificationbell);
        }

        if(mEventType == GEEventTypes.EFetchEventsLive){
            lDateTimeStr = "LIVE NOW";
            lListItem.mDetailView.setTextColor(Color.parseColor("#ff0021"));
        }

        lListItem.mDetailView.setText(lDateTimeStr);

        ThumbnailDetails lThumbUrls = lResult.getSnippet().getThumbnails();
        Thumbnail lThumbnail = lThumbUrls.getHigh();
        String lUrl = lThumbnail .getUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();
        // Load image, decode it to Bitmap and return Bitmap to callback
        File file = ImageLoader.getInstance().getDiskCache().get(lUrl);
        if (file==null) {
            //Load image from network
            try {
                InputStream lInputStream = mContext.getAssets().open("images/loadingthumbnailurl.png");
                Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
                lListItem.mImageView.setImageBitmap(lBitmap);
                imageLoader.displayImage(lUrl, lListItem.mImageView);
            } catch (IOException e) {
//            handle exception
            }
            imageLoader.displayImage(lUrl, lListItem.mImageView);
        }
        else {
            //Load image from cache
            lListItem.mImageView.setImageURI(Uri.parse(file.getAbsolutePath()));
        }

        if (position == listPages.size()*50-1)
        {
            mLoadMoreListner.loadMoreItems(this);
        }
    }

    @Override
    public GEEventListItemView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater lInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup lMainGroup = null;
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, GEConstants.GECHANNELID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        lMainGroup = (ViewGroup) lInflater.inflate(gala.com.kidstv.R.layout.ge_listitem, viewGroup, false);
        if (mEventType == GEEventTypes.EFetchEventsReminders) {
            ViewGroup.LayoutParams lParams = lMainGroup.getLayoutParams();
            lParams.width = RecyclerView.LayoutParams.MATCH_PARENT;
        }
        GEEventListItemView listHolder = new GEEventListItemView(lMainGroup, mClickListner, mEventType);
        return listHolder;
    }

    private String fomateDateStr(String dateTimeStr)
    {
        SimpleDateFormat lSd1 = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'");
        Date lDate = null;
        try {
            lDate = lSd1.parse(dateTimeStr);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
        SimpleDateFormat lSD2 = new SimpleDateFormat("EEE, dd MMM yyyy, hh:mm a");
        String lNewDateTimeStr = lSD2.format(lDate);
        return lNewDateTimeStr;
    }
}