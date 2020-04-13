package gala.com.kidstv.Refactored.items.PosterItems;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class PosterRenderer extends ViewRenderer<PosterModel, PosterViewHolder> {

    @NonNull
    private final MediaRenderer.Listener mListener;

    public PosterRenderer(@NonNull final MediaRenderer.Listener listener) {
        super(gala.com.kidstv.Refactored.items.PosterItems.PosterModel.class);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final PosterModel model, @NonNull final PosterViewHolder holder) {
//        holder.mName.setText(model.mediaName());
//        holder.mMediaDesc.setText(model.mediaDescription());
//        holder.mMediaTypeStr.setText(model.mediaTypeStr());
//        holder.mViewAll.setOnClickListener(v -> mListener.onCategoryClicked(model));
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
