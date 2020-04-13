package gala.com.kidstv.Refactored.items.ThumbnailItems;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class ThumbnailRenderer extends ViewRenderer<ThumbnailModel, ThumbnailViewHolder> {

    @NonNull
    private final MediaRenderer.Listener mListener;

    public ThumbnailRenderer(@NonNull final MediaRenderer.Listener listener) {
        super(ThumbnailModel.class);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final ThumbnailModel model, @NonNull final ThumbnailViewHolder holder) {
//        holder.mName.setText(model.mediaName());
//        holder.mMediaDesc.setText(model.mediaDescription());
//        holder.mMediaTypeStr.setText(model.mediaTypeStr());
//        holder.mViewAll.setOnClickListener(v -> mListener.onCategoryClicked(model));
    }

    @NonNull
    @Override
    public ThumbnailViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new ThumbnailViewHolder(inflate(R.layout.thumbnail_view_holder, parent));
    }

    public interface Listener {
        void onCategoryClicked(@NonNull MediaModel model);
    }
}
