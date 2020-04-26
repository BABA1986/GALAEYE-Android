package gala.com.kidstv.Refactored.items.ThumbnailItems;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class ThumbnailViewHolder extends ViewHolder {
    public final TextView mTvTitle;
    public final TextView mTvDuration;
    public final ImageView mIvThumbnail;

    public ThumbnailViewHolder(final View itemView) {
        super(itemView);
        mTvTitle = itemView.findViewById(R.id.tv_title);
        mTvDuration = itemView.findViewById(R.id.tv_duration);
        mIvThumbnail  = itemView.findViewById(R.id.iv_thumbnail);
    }
}