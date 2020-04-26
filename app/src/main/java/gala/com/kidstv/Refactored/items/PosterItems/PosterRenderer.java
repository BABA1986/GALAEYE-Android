package gala.com.kidstv.Refactored.items.PosterItems;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class PosterRenderer extends ViewRenderer<PosterModel, PosterViewHolder> {

    private final Context mContext;

    public PosterRenderer(Context context) {
        super(gala.com.kidstv.Refactored.items.PosterItems.PosterModel.class);
        mContext = context;
    }

    @Override
    public void bindView(@NonNull final PosterModel model, @NonNull final PosterViewHolder holder) {
        if (model.getMediaLargeIcon()!=null){
            Glide.with(mContext).load(model.getMediaLargeIcon()).into(holder.mIvPoster);
        }
    }

    @NonNull
    @Override
    public PosterViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new PosterViewHolder(inflate(R.layout.poster_view_holder, parent));
    }

    public interface Listener {
        void onCategoryClicked(@NonNull MediaModel model);
    }
}
