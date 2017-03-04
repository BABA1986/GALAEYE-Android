package android.com.galatube.GEPlaylist;

import android.com.galatube.GEYoutubeEvents.GEEventListItemView;
import android.com.galatube.GEYoutubeEvents.GEEventListObj;
import android.com.galatube.GEYoutubeEvents.GEEventListPage;
import android.com.galatube.GEYoutubeEvents.GEEventManager;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.GEYoutubeEvents.GEOnLoadMore;
import android.com.galatube.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import com.google.api.services.youtube.model.PlaylistItem;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by deepak on 02/03/17.
 */

public class GEPlaylistVideolistAdapter extends
        RecyclerView.Adapter<GEEventListItemView> {

    private Context mContext;
    private GEOnLoadMore mLoadMoreListner;
    private String mPlaylistID;

    public GEPlaylistVideolistAdapter(Context context, String playlistID, GEOnLoadMore loadmoreListner) {
        this.mContext = context;
        this.mLoadMoreListner = loadmoreListner;
        this.mPlaylistID = playlistID;
    }

    public String getmPlaylistID() {
        return mPlaylistID;
    }

    @Override
    public int getItemCount() {
        GEVideoListManager lMamager = GEVideoListManager.getInstance();
        GEVideolistObj listObj = lMamager.playlistVideoObjForInfo(mPlaylistID);
        if (listObj == null) return 0;
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        GEVideoListPage lPage = listPages.get(listPages.size() - 1);
        List<PlaylistItem> lResults = lPage.getmPlayListItems();
        if (lResults.size() < 50 && listPages.size() == 1)
            return lResults.size();
        else if (lResults.size() < 50 && listPages.size() > 1)
            return (listPages.size() - 1) * 50 + lResults.size();

        return listPages.size() * 50;
    }

    @Override
    public void onBindViewHolder(GEEventListItemView holder, int position) {
        GEEventListItemView lListItem = (GEEventListItemView) holder;// holder

        GEVideoListManager lMamager = GEVideoListManager.getInstance();
        GEVideolistObj listObj = lMamager.playlistVideoObjForInfo(mPlaylistID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();
        int lPageIndex = (position >= 50) ? position / 50 : 0;
        GEVideoListPage lPage = listPages.get(listPages.size() - 1);
        List<PlaylistItem> lResults = lPage.getmPlayListItems();
        int lPosition = position - lPageIndex * 50;
        PlaylistItem lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());

        ThumbnailDetails lThumbUrls = lResult.getSnippet().getThumbnails();
        Thumbnail lThumbnail = lThumbUrls.getHigh();
        String lUrl = lThumbnail.getUrl();
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

        if (position == listPages.size() * 50 - 1) {
            mLoadMoreListner.loadMoreItems(this);
        }
    }

    @Override
    public GEEventListItemView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater lInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup lMainGroup = null;
        GEVideoListManager lMamager = GEVideoListManager.getInstance();
        GEVideolistObj listObj = lMamager.playlistVideoObjForInfo(mPlaylistID);
        ArrayList<GEVideoListPage> listPages = listObj.getmVideoListPages();

        lMainGroup = (ViewGroup) lInflater.inflate(
                R.layout.gevideoitem, viewGroup, false);
        GEEventListItemView listHolder = new GEEventListItemView(lMainGroup);
        return listHolder;
    }
}
