package android.com.galatube.GEYoutubeEvents;

import android.com.galatube.*;
import android.com.galatube.GETheme.GEThemeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.format.DateUtils;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import parallaxrecyclerview.ParallaxRecyclerAdapter;

/**
 * Created by deepak on 29/01/17.
 */

public class GEPopularEventListAdapter extends ParallaxRecyclerAdapter<GEEventListItemView> {

    private Context         mContext;
    private GEOnLoadMore    mLoadMoreListner;
    private GEEventTypes    mEventType;
    private String          mChannelId;
    private GERecyclerItemClickListner mItemClickListner;



    public GEPopularEventListAdapter(Context context, GEEventTypes eventType, GEOnLoadMore loadmoreListner, String channelId, GERecyclerItemClickListner itemClickListner) {
        super(null);
        this.mContext = context;
        this.mItemClickListner = itemClickListner;
        this.mLoadMoreListner = loadmoreListner;
        this.mEventType = eventType;
        this.mChannelId = channelId;
    }

    public GEEventTypes getmEventType() {
        return mEventType;
    }

    @Override
    public int getItemCountImpl(ParallaxRecyclerAdapter<GEEventListItemView> adapter) {
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, mChannelId);
        if (listObj == null) return 0;
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        GEVideoListPage lPage = listPages.get(listPages.size() - 1);
        List<Video> lResults = lPage.getmVideoList();
        if (lResults.size() < 50 && listPages.size() == 1)
            return lResults.size();
        else if (lResults.size() < 50 && listPages.size() > 1)
            return (listPages.size()-1)*50 + lResults.size();

        return listPages.size()*50;
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<GEEventListItemView> adapter, int position) {
        GEEventListItemView lListItem = (GEEventListItemView) viewHolder;// holder

        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, mChannelId);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = position - lPageIndex*50;
        Video lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());

        int lViewCount = lResult.getStatistics().getViewCount().intValue();
        DateTime lPublishedOn = lResult.getSnippet().getPublishedAt();
        long lTimeStamp = lPublishedOn.getValue();
        long lCurrTimeStamp = new Date().getTime();
        long lDeff = lCurrTimeStamp - lTimeStamp;
        String lAgoString = GEDateUtils.getTimeAgo(lDeff);

        String lViewCountStr = GENumFormatter.format(lViewCount);
        String lDetail = lResult.getSnippet().getChannelTitle();
        lDetail = lDetail + " • " + lViewCountStr + " Views " + "•" + lAgoString;
        lListItem.mDetailView.setText(lDetail);

        ThumbnailDetails lThumbUrls = lResult.getSnippet().getThumbnails();
        Thumbnail lThumbnail = lThumbUrls.getHigh();
        String lUrl = lThumbnail.getUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();
        // Load image, decode it to Bitmap and return Bitmap to callback
        File file = ImageLoader.getInstance().getDiskCache().get(lUrl);
        if (file==null) {
            //Load image from network
            SharedPreferences sharedPreferences = mContext.getSharedPreferences("myTheme", Context.MODE_PRIVATE);
            GEThemeManager.getInstance(mContext).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
            int lColor = GEThemeManager.getInstance(mContext).getSelectedNavColor();
            Resources resources = mContext.getResources();
            final int resourceId = resources.getIdentifier("urtubeplaceholder", "drawable",
                    mContext.getPackageName());
            Drawable lDrawable = resources.getDrawable(resourceId);

            Bitmap lBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                    resourceId);
            lListItem.mPlaceholderImageView.setImageBitmap(lBitmap);
            changeBitmapColor(lBitmap, lListItem.mPlaceholderImageView, lColor);
            lListItem.mImageView.setImageBitmap(null);
            lListItem.mImageView.setBackgroundColor(Color.TRANSPARENT);
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
    public RecyclerView.ViewHolder onCreateViewHolderImpl(ViewGroup viewGroup, ParallaxRecyclerAdapter<GEEventListItemView> adapter, int i) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater lInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup lMainGroup = null;
        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, mChannelId);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();

        lMainGroup = (ViewGroup) lInflater.inflate(
                R.layout.gevideoitem, viewGroup, false);
        GEEventListItemView listHolder = new GEEventListItemView(lMainGroup,mItemClickListner, mEventType);
        return listHolder;
    }

    private void changeBitmapColor(Bitmap sourceBitmap, ImageView image, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        p.setColorFilter(filter);
        image.setImageBitmap(resultBitmap);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
    }
}