package gala.com.kidstv.Refactored.items.PagerItems;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class PagerViewHolder extends ViewHolder {
    public final ImageView mIvMedia;
    public final TextView mTvCount;
    public final View mItemView;

    public PagerViewHolder(final View itemView) {
        super(itemView);
        mItemView = itemView;
        mIvMedia = itemView.findViewById(R.id.iv_media);
        mTvCount = itemView.findViewById(R.id.tv_count);
    }
}