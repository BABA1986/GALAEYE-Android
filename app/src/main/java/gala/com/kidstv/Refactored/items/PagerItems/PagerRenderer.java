package gala.com.kidstv.Refactored.items.PagerItems;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;


public class PagerRenderer extends ViewRenderer<PagerModel, PagerViewHolder> {

    @NonNull
    private final MediaRenderer.Listener mListener;

    public PagerRenderer(@NonNull final MediaRenderer.Listener listener) {
        super(PagerModel.class);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final PagerModel model, @NonNull final PagerViewHolder holder) {
//        holder.mName.setText(model.mediaName());
//        holder.mMediaDesc.setText(model.mediaDescription());
//        holder.mMediaTypeStr.setText(model.mediaTypeStr());
//        holder.mViewAll.setOnClickListener(v -> mListener.onCategoryClicked(model));
    }

    @NonNull
    @Override
    public PagerViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new PagerViewHolder(inflate(R.layout.pager_view_holder, parent));
    }

    public interface Listener {
        void onCategoryClicked(@NonNull MediaModel model);
    }
}
