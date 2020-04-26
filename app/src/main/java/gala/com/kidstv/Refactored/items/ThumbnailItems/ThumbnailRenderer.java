package gala.com.kidstv.Refactored.items.ThumbnailItems;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class ThumbnailRenderer extends ViewRenderer<ThumbnailModel, ThumbnailViewHolder> {

    private final Context mContext;

    public ThumbnailRenderer(Context context) {
        super(ThumbnailModel.class);
        mContext = context;
    }

    @Override
    public void bindView(@NonNull final ThumbnailModel model, @NonNull final ThumbnailViewHolder holder) {
        if (model.getMediaLargeIcon()!=null){
            Glide.with(mContext).load(model.getMediaLargeIcon()).into(holder.mIvThumbnail);
        }
        holder.mTvTitle.setText(model.mediaDescription());
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
