package gala.com.kidstv.Refactored.items.PosterItems;

import android.view.View;
import android.widget.ImageView;

import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class PosterViewHolder extends ViewHolder {
    private final ImageView mIvPoster;

    public PosterViewHolder(final View itemView) {
        super(itemView);
        mIvPoster = itemView.findViewById(R.id.iv_poster);
    }
}