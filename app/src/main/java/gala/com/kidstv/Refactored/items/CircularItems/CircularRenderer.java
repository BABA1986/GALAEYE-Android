package gala.com.kidstv.Refactored.items.CircularItems;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;
import gala.com.kidstv.Refactored.items.PagerItems.PagerModel;
import gala.com.kidstv.Refactored.items.PagerItems.PagerViewHolder;

public class CircularRenderer extends ViewRenderer<CircularModel, CircularViewHolder> {

    private final Context mContext;

    public CircularRenderer(Context context) {
        super(CircularModel.class);
        mContext = context;
    }

    @Override
    public void bindView(@NonNull final CircularModel model, @NonNull final CircularViewHolder holder) {
        if (model.getMediaLargeIcon()!=null){
            Glide.with(mContext).load(model.getMediaLargeIcon()).into(holder.mIvPoster);
        }
        holder.mTvPosterName.setText(model.mediaName());
    }

    @NonNull
    @Override
    public CircularViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new CircularViewHolder(inflate(R.layout.circular_view_holder, parent));
    }

    public interface Listener {
        void onCategoryClicked(@NonNull MediaModel model);
    }
}
