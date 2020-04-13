package gala.com.kidstv.GEPlaylist;

import gala.com.kidstv.GETheme.GEThemeManager;
import gala.com.kidstv.GEYoutubeEvents.GERecyclerItemClickListner;
import gala.com.kidstv.R;
import android.content.Context;
import android.content.SharedPreferences;
import androidx.recyclerview.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

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
           Context                      mContext;

    public GEPlaylistListItemView(View view,Context context ,GERecyclerItemClickListner clickListner){
        super(view);
        // Find all views ids
        this.mContext=context;
        this.mTitleView = (TextView)view.findViewById(R.id.playlistitemtitle);
        this.mImageView = (ImageView)view.findViewById(R.id.playlistitemimage);
        this.mVideoCount = (TextView)view.findViewById(R.id.videoscount);
        this.mVideoTagLbl = (TextView)view.findViewById(R.id.videotag);
        this.mPlaylistRightLayout = (RelativeLayout) view.findViewById(R.id.playlistrightbase);
        this.mPlaylistIndicatorImgView = (ImageView) view.findViewById(R.id.playlisticonimage);
        this.mClickListner = clickListner;
        SharedPreferences sharedPreferences=mContext.getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(mContext).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(mContext).getSelectedNavColor();
        mPlaylistRightLayout.setBackgroundColor(lColor);

        view.setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        mClickListner.onRecyclerItemClicked(v, this, getAdapterPosition());
    }
}
