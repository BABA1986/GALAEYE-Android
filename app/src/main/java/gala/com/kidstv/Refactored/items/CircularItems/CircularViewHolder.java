package gala.com.kidstv.Refactored.items.CircularItems;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class CircularViewHolder extends ViewHolder {
    public final ImageView mIvPoster;
    public final TextView mTvPosterName;

    public CircularViewHolder(final View itemView) {
        super(itemView);
        mIvPoster = itemView.findViewById(R.id.iv_circuler);
        mTvPosterName = itemView.findViewById(R.id.tv_circuler);
    }
}
