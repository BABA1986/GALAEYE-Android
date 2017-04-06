package android.com.galatube.GEPlaylist;

import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.GEYoutubeEvents.GEOnLoadMore;
import android.com.galatube.GEYoutubeEvents.GERecyclerItemClickListner;
import android.com.galatube.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.RelativeLayout;

import com.google.api.services.youtube.model.Playlist;
import com.google.api.services.youtube.model.Thumbnail;
import com.google.api.services.youtube.model.ThumbnailDetails;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by deepak on 20/02/17.
 */

public class GEPlaylistListAdapter extends  RecyclerView.Adapter<GEPlaylistListItemView>{
    private Context                     mContext;
    private GEOnLoadMore                mLoadMoreListner;
    private String                      mChannelName;
    private GERecyclerItemClickListner  mClickListener;
    private RelativeLayout mGEplayListVideo;

    public GEPlaylistListAdapter(Context context,
                                 GEOnLoadMore loadmoreListner,
                                 GERecyclerItemClickListner clickListner,
                                 String channelName)
    {
        this.mContext = context;
        this.mLoadMoreListner = loadmoreListner;
        this.mClickListener = clickListner;
        this.mChannelName = channelName;
    }

    @Override
    public int getItemCount() {
        GEPlaylistManager lMamager = GEPlaylistManager.getInstance();
        GEPlaylistObj listObj = lMamager.playlistListObjForInfo(mChannelName);
        if (listObj == null) return 0;
        ArrayList<GEPlaylistPage> listPages = listObj.getmPlayListListPages();
        GEPlaylistPage lPage = listPages.get(listPages.size() - 1);
        List<Playlist> lResults = lPage.getmPlaylistList();
        if (lResults.size() < 50 && listPages.size() == 1)
            return lResults.size();
        else if (lResults.size() < 50 && listPages.size() > 1)
            return (listPages.size()-1)*50 + lResults.size();

        return listPages.size()*50;
    }

    @Override
    public void onBindViewHolder(GEPlaylistListItemView holder, int position) {
        GEPlaylistListItemView lListItem = (GEPlaylistListItemView) holder;// holder
        GEPlaylistManager lMamager = GEPlaylistManager.getInstance();
        GEPlaylistObj listObj = lMamager.playlistListObjForInfo(mChannelName);
        ArrayList<GEPlaylistPage> listPages = listObj.getmPlayListListPages();
        int lPageIndex = (position >= 50) ? position/50 : 0;
        GEPlaylistPage lPage = listPages.get(lPageIndex);
        List<Playlist> lResults = lPage.getmPlaylistList();
        int lPosition = position - lPageIndex*50;
        Playlist lResult = lResults.get(lPosition);
        lListItem.mTitleView.setText(lResult.getSnippet().getTitle());
        long lItemCounts = lResult.getContentDetails().getItemCount();
        lListItem.mVideoCount.setText(Long.toString(lItemCounts));

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
    public GEPlaylistListItemView onCreateViewHolder(ViewGroup viewGroup, int viewType) {
        // This method will inflate the custom layout and return as viewholder
        LayoutInflater lInflater = LayoutInflater.from(viewGroup.getContext());
        ViewGroup lMainGroup = null;
        GEPlaylistManager lMamager = GEPlaylistManager.getInstance();
        GEPlaylistObj listObj = lMamager.playlistListObjForInfo(mChannelName);
        ArrayList<GEPlaylistPage> listPages = listObj.getmPlayListListPages();
        lMainGroup = (ViewGroup) lInflater.inflate(R.layout.ge_playlistitem, viewGroup, false);
        GEPlaylistListItemView listHolder = new GEPlaylistListItemView(lMainGroup,mContext, mClickListener);

        try {
            InputStream lInputStream = mContext.getAssets().open("images/playlistIcon.png");
            Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
            listHolder.mPlaylistIndicatorImgView.setImageBitmap(lBitmap);
        } catch (IOException e) {
//            handle exception
        }
         return listHolder;
    }
}
