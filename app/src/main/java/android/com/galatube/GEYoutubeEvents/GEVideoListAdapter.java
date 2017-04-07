package android.com.galatube.GEYoutubeEvents;

import android.com.galatube.*;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 29/01/17.
 */

public class GEVideoListAdapter extends
        RecyclerView.Adapter<GEEventListItemView> {

    private Context mContext;
    private GEOnLoadMore    mLoadMoreListner;
    private GEEventTypes    mEventType;
    private String          mChannelId;

    public GEVideoListAdapter(Context context, GEEventTypes eventType, GEOnLoadMore loadmoreListner, String channelId) {
        this.mContext = context;
        this.mLoadMoreListner = loadmoreListner;
        this.mEventType = eventType;
        this.mChannelId = channelId;
    }

    public GEEventTypes getmEventType() {
        return mEventType;
    }

    @Override
    public int getItemCount() {
        GEEventManager lMamager = GEEventManager.getInstance();
        GEEventListObj listObj = lMamager.eventListObjForInfo(mEventType, mChannelId);
        if (listObj == null) return 0;
        ArrayList<GEEventListPage> listPages = listObj.getmEventListPages();
        GEEventListPage lPage = listPages.get(listPages.size() - 1);
        List<SearchResult> lResults = lPage.getmEventList();
        if (lResults.size() < 50 && listPages.size() == 1)
            return lResults.size();
        else if (lResults.size() < 50 && listPages.size() > 1)
            return (listPages.size()-1)*50 + lResults.size();

        return listPages.size()*50;
    }

    @Override
    public void onBindViewHolder(GEEventListItemView holder, int position) {
        GEEventListItemView lListItem = (GEEventListItemView) holder;// holder

        GEEventManager lMamager = GEEventManager.getInstance();
        GEEventListObj listObj = lMamager.eventListObjForInfo(mEventType, mChannelId);
        ArrayList<GEEventListPage> listPages = listObj.getmEventListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEEventListPage lPage = listPages.get(lPageIndex);
        List<SearchResult> lResults = lPage.getmEventList();
        int lPosition = position - lPageIndex*50;
        SearchResult lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());

        ThumbnailDetails lThumbUrls = lResult.getSnippet().getThumbnails();
        Thumbnail lThumbnail = lThumbUrls.getHigh();
        String lUrl = lThumbnail .getUrl();
        ImageLoader imageLoader = ImageLoader.getInstance();
        // Load image, decode it to Bitmap and return Bitmap to callback
        try {
            InputStream lInputStream = mContext.getAssets().open("images/loadingthumbnailurl.png");
            Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
            lListItem.mImageView.setImageBitmap(lBitmap);
            imageLoader.displayImage(lUrl, lListItem.mImageView);
        } catch (IOException e) {
//            handle exception
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
        GEEventListObj listObj = lMamager.eventListObjForInfo(mEventType, mChannelId);
        ArrayList<GEEventListPage> listPages = listObj.getmEventListPages();

        lMainGroup = (ViewGroup) lInflater.inflate(
                R.layout.gevideoitem, viewGroup, false);
        GEEventListItemView listHolder = new GEEventListItemView(lMainGroup);
        return listHolder;
    }
}