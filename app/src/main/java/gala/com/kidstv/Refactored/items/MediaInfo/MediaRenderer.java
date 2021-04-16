package gala.com.kidstv.Refactored.items.MediaInfo;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.CallBackListener;

public class MediaRenderer extends ViewRenderer<MediaModel, MediaViewHolder> {

    @NonNull
    private CallBackListener mListener;

    public MediaRenderer(@NonNull CallBackListener listener) {
        super(MediaModel.class);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final MediaModel model, @NonNull final MediaViewHolder holder) {
        holder.mName.setText(model.mediaName());
//        holder.mMediaDesc.setText(model.mediaDescription());
        holder.mMediaTypeStr.setText(model.mediaTypeStr());
//        holder.mViewAll.setOnClickListener(v -> mListener.onCategoryClicked(model));
    }

    @NonNull
    @Override
    public MediaViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new MediaViewHolder(inflate(R.layout.media_view_holder, parent));
    }
}
