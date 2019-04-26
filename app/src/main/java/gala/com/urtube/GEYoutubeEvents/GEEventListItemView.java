package gala.com.urtube.GEYoutubeEvents;

import gala.com.urtube.R;

import android.graphics.Color;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

/**
 * Created by deepak on 16/01/17.
 */
class LoadingViewHolder extends RecyclerView.ViewHolder {
    public ProgressBar progressBar;

    public LoadingViewHolder(View view) {
        super(view);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar1);
    }
}

public class GEEventListItemView extends RecyclerView.ViewHolder implements View.OnClickListener
{
    // View holder for gridview recycler view as we used in listview
    public TextView                     mTitleView;
    public TextView                     mDetailView;
    public TextView                     mDurationView;
    public ImageView                    mImageView;
    public ImageView                    mPlaceholderImageView;
    public ImageButton                  mNotificationBtnForTouch;
    public ImageButton                  mNotificationBtn;
    private GERecyclerItemClickListner  mClickListner;
    private GEEventTypes                mEventType;

    public GEEventListItemView(View view, GERecyclerItemClickListner clickListner, GEEventTypes eventType) {
        super(view);
        // Find all views ids

        this.mTitleView = (TextView) view.findViewById(R.id.title);
        this.mDetailView = (TextView) view.findViewById(R.id.detail);
        this.mDurationView = (TextView) view.findViewById(R.id.durationtextview);
        this.mImageView = (ImageView) view.findViewById(R.id.image);
        this.mPlaceholderImageView = (ImageView)view.findViewById(R.id.placeholderimage);
        this.mNotificationBtnForTouch = (ImageButton) view.findViewById(R.id.notificationtouchbtn);
        this.mNotificationBtn = (ImageButton) view.findViewById(R.id.notificationbtn);
        this.mClickListner = clickListner;
        this.mEventType = eventType;
        this.mImageView.setBackgroundColor(Color.TRANSPARENT);

        if (this.mNotificationBtnForTouch != null)
         this.mNotificationBtnForTouch.setOnClickListener(this);
        view.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        mClickListner.onRecyclerItemClicked(v, this, getAdapterPosition(), mEventType);
    }
}
