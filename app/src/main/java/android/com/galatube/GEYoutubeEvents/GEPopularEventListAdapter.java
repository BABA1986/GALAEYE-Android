package android.com.galatube.GEYoutubeEvents;

import android.com.galatube.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.google.api.services.youtube.model.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
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
}