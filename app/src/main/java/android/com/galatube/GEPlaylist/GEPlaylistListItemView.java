package android.com.galatube.GEPlaylist;

import android.com.galatube.GEYoutubeEvents.GERecyclerItemClickListner;
import android.com.galatube.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

/**
 * Created by deepak on 20/02/17.
 */

public class GEPlaylistListItemView extends RecyclerView.ViewHolder implements View.OnClickListener
{
    // View holder for gridview recycler view as we used in listview
    public TextView                     mTitleView;
    public ImageView                    mImageView;
    public TextView                     mVideoCount;
    public RelativeLayout               mPlaylistRightLayout;
    public TextView                     mVideoTagLbl;
    public ImageView                    mPlaylistIndicatorImgView;
    private GERecyclerItemClickListner  mClickListner;

    public GEPlaylistListItemView(View view, GERecyclerItemClickListner clickListner){
        super(view);
        // Find all views ids
        this.mTitleView = (TextView)view.findViewById(R.id.playlistitemtitle);
        this.mImageView = (ImageView)view.findViewById(R.id.playlistitemimage);
        this.mVideoCount = (TextView)view.findViewById(R.id.videoscount);
        this.mVideoTagLbl = (TextView)view.findViewById(R.id.videotag);
        this.mPlaylistRightLayout = (RelativeLayout) view.findViewById(R.id.playlistrightbase);
        this.mPlaylistIndicatorImgView = (ImageView) view.findViewById(R.id.playlisticonimage);
        this.mClickListner = clickListner;
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickListner.onRecyclerItemClicked(v, this, getAdapterPosition());
    }
}
