package gala.com.kidstv.GEYoutubeEvents;

import gala.com.kidstv.GEConstants;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 09/04/17.
 */

public class GELiveEventListAdapter extends
        RecyclerView.Adapter<GELiveEventListItemView> {
    private Context mContext;
    private GEOnLoadMore    mLoadMoreListner;
    private GEEventTypes    mEventType;

    public GELiveEventListAdapter(Context context, GEEventTypes eventType, GEOnLoadMore loadmoreListner) {
        this.mContext = context;
        this.mLoadMoreListner = loadmoreListner;
        this.mEventType = eventType;
    }

    public GEEventTypes getmEventType() {
        return mEventType;
    }

    @Override
    public GELiveEventListItemView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        LayoutInflater lInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup lMainGroup = null;
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, GEConstants.GECHANNELID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        lMainGroup = (ViewGroup) lInflater.inflate(gala.com.kidstv.R.layout.ge_livelistitem, viewGroup, false);
        GELiveEventListItemView listHolder = new GELiveEventListItemView(lMainGroup);
        return listHolder;
    }

    @Override
    public void onBindViewHolder(GELiveEventListItemView holder, int position) {
        GELiveEventListItemView lListItem = (GELiveEventListItemView) holder;// holder
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, GEConstants.GECHANNELID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = position - lPageIndex*50;
        Video lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());
        String lDateTimeStr = lResult.getLiveStreamingDetails().getScheduledStartTime().toString();
        if(mEventType == GEEventTypes.EFetchEventsLive){
            lDateTimeStr = "LIVE NOW";
            lListItem.mDateTime.setTextColor(Color.parseColor("#ff0021"));
        }
        lListItem.mDateTime.setText(lDateTimeStr);

//        lListItem.mDateTime.setText(lResult.getSnippet().getPublishedAt().toString());
        ThumbnailDetails lThumbUrls = lResult.getSnippet().getThumbnails();
        Thumbnail lThumbnail = lThumbUrls.getHigh();
        String lUrl = lThumbnail .getUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();

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
        // Load image, decode it to Bitmap and return Bitmap to callback

        if (position == listPages.size()*50-1)
        {
            mLoadMoreListner.loadMoreItems(this);
        }

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
}
