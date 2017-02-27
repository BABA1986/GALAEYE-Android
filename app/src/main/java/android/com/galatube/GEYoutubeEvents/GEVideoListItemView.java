package android.com.galatube.GEYoutubeEvents;

import android.com.galatube.R;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by deepak on 29/01/17.
 */

public class GEVideoListItemView extends RecyclerView.ViewHolder {
    // View holder for gridview recycler view as we used in listview
    public TextView mTitleView;
    public ImageView mImageView;

    public GEVideoListItemView(View view){
        super(view);
        // Find all views ids
        this.mTitleView =(TextView)view.findViewById(R.id.title);
        this.mImageView=(ImageView)view.findViewById(R.id.image);
    }
}