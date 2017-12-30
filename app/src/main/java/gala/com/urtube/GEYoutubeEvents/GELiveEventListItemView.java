package gala.com.urtube.GEYoutubeEvents;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 09/04/17.
 */
public class GELiveEventListItemView extends RecyclerView.ViewHolder{
    public TextView mTitleView;
    public TextView mDateTime;
    public ImageView mImageView;

    public GELiveEventListItemView(View view) {
        super(view);
        this.mTitleView = (TextView) view.findViewById(gala.com.urtube.R.id.title);
        this.mDateTime = (TextView) view.findViewById(gala.com.urtube.R.id.datetime);
        this.mImageView = (ImageView) view.findViewById(gala.com.urtube.R.id.image);
    }
}
