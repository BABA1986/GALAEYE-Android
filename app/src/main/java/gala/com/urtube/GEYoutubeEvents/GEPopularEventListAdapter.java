package gala.com.urtube.GEYoutubeEvents;

import gala.com.urtube.GETheme.GEThemeManager;
import gala.com.urtube.R;
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
import android.graphics.drawable.StateListDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import parallaxrecyclerview.ParallaxRecyclerAdapter;

public class GEPopularEventListAdapter extends ParallaxRecyclerAdapter<GEEventListItemView> {

    private Context         mContext;
    private GEOnLoadMore    mLoadMoreListner;
    private GEEventTypes    mEventType;
    private String          mChannelId;
    private GERecyclerItemClickListner mItemClickListner;
    private GEItemType                 mGEItemType;

    public static final int LOADING_VIEW = 4;

    public GEPopularEventListAdapter(Context context, GEEventTypes eventType, GEOnLoadMore loadmoreListner, String channelId, GERecyclerItemClickListner itemClickListner) {
        super(null);
        this.mContext = context;
        this.mItemClickListner = itemClickListner;
        this.mLoadMoreListner = loadmoreListner;
        this.mEventType = eventType;
        this.mChannelId = channelId;
        this.mGEItemType = GEItemType.EItemTypeDefault;
    }

    public GEPopularEventListAdapter(Context context, GEEventTypes eventType, GEOnLoadMore loadmoreListner, String channelId, GERecyclerItemClickListner itemClickListner, GEItemType itemType) {
        super(null);
        this.mContext = context;
        this.mItemClickListner = itemClickListner;
        this.mLoadMoreListner = loadmoreListner;
        this.mEventType = eventType;
        this.mChannelId = channelId;
        this.mGEItemType = GEItemType.EItemTypeDefault;
        this.mGEItemType = itemType;
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

        if (lResults.size() < 50 && listPages.size() == 1) {
            if (lPage.getmNextPageToken() != null)
                return lResults.size() + 1;
            return lResults.size();
        }
        else if (lResults.size() < 50 && listPages.size() > 1)
            return (listPages.size()-1)*50 + lResults.size();

        if (lPage.getmNextPageToken() != null)
            return listPages.size()*50 + 1;

        return listPages.size()*50;
    }

    @Override
    public void onBindViewHolderImpl(RecyclerView.ViewHolder viewHolder, ParallaxRecyclerAdapter<GEEventListItemView> adapter, int position) {
        if (viewHolder instanceof LoadingViewHolder)
            return;

        GEEventListItemView lListItem = (GEEventListItemView) viewHolder;// holder

        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, mChannelId);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        if (lPageIndex >= listPages.size()) return;
        GEVideoListPage lPage = listPages.get(lPageIndex);
        List<Video> lResults = lPage.getmVideoList();
        int lPosition = position - lPageIndex*50;
        Video lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myTheme", Context.MODE_PRIVATE);
        GEThemeManager.getInstance(mContext).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(mContext).getSelectedNavColor();
        int lTextColor = GEThemeManager.getInstance(mContext).getSelectedNavTextColor();

        int lViewCount = lResult.getStatistics().getViewCount().intValue();
        DateTime lPublishedOn = lResult.getSnippet().getPublishedAt();
        long lTimeStamp = lPublishedOn.getValue();
        long lCurrTimeStamp = new Date().getTime();
        long lDeff = lCurrTimeStamp - lTimeStamp;
        String lAgoString = GEDateUtils.getTimeAgo(lDeff);

        String lViewCountStr = GENumFormatter.format(lViewCount);
        String lDetail = lResult.getSnippet().getChannelTitle();
        if (lDetail.length() > 14) {
            lDetail = lDetail.substring(0, 14);
            lDetail = lDetail + "....";
        }

        lDetail = lDetail  + " • " + lViewCountStr + " Views " + "• " + lAgoString;
        lListItem.mDetailView.setText(lDetail);

        String lDuration = lResult.getContentDetails().getDuration();
        lDuration = lDuration.split("S")[0];
        lDuration = lDuration.replace("PT", "").replace("M", ":");
        lDuration = " " + lDuration + " ";
        lListItem.mDurationView.setText(lDuration);
        StateListDrawable bgShape = (StateListDrawable)lListItem.mDurationView.getBackground();
        bgShape.setColorFilter(lColor, PorterDuff.Mode.SRC_IN);
        lListItem.mDurationView.setTextColor(lTextColor);

        ThumbnailDetails lThumbUrls = lResult.getSnippet().getThumbnails();
        Thumbnail lThumbnail = lThumbUrls.getHigh();
        String lUrl = lThumbnail.getUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();
        // Load image, decode it to Bitmap and return Bitmap to callback
        File file = ImageLoader.getInstance().getDiskCache().get(lUrl);
        if (file==null) {
            //Load image from network
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

        if (i == LOADING_VIEW)
        {
            lMainGroup = (ViewGroup) lInflater.inflate(
                    R.layout.itemloading, viewGroup, false);
            LoadingViewHolder listHolder = new LoadingViewHolder(lMainGroup);
            return listHolder;
        }

        if (mGEItemType == GEItemType.EItemTypeList) {
            lMainGroup = (ViewGroup) lInflater.inflate(
                    R.layout.gevideolistitem, viewGroup, false);
        }
        else {
            lMainGroup = (ViewGroup) lInflater.inflate(
                    R.layout.gevideoitem, viewGroup, false);
        }

        GEEventListItemView listHolder = new GEEventListItemView(lMainGroup, mItemClickListner, mEventType);
        return listHolder;
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 1)
            return VIEW_TYPES.FIRST_VIEW;

        if(position == 0 && hasHeader())
            return  VIEW_TYPES.HEADER;

        GEEventManager lMamager = GEEventManager.getInstance();
        GEVideoListObj listObj = lMamager.videoListObjForInfo(mEventType, mChannelId);
        if (listObj == null) return VIEW_TYPES.NORMAL;

        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        GEVideoListPage lPage = listPages.get(listPages.size() - 1);
        List<Video> lResults = lPage.getmVideoList();

        if (position == listPages.size()*50 + 1 && lPage.getmNextPageToken() != null)
            return LOADING_VIEW;

        return VIEW_TYPES.NORMAL;
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