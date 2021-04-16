package gala.com.kidstv.Refactored.items.ThumbnailItems;

import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class ThumbnailViewHolder extends ViewHolder {
    public final TextView mTvTitle;
    public final TextView mTvDuration;
    public final ImageView mIvThumbnail;
    public final RelativeLayout mRrContainer;

    public ThumbnailViewHolder(final View itemView) {
        super(itemView);
        mRrContainer = itemView.findViewById(R.id.rr_container);
        mTvTitle = itemView.findViewById(R.id.tv_title);
        mTvDuration = itemView.findViewById(R.id.tv_duration);
        mIvThumbnail  = itemView.findViewById(R.id.iv_thumbnail);
    }
}