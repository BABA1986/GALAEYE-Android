package gala.com.kidstv.Refactored.items.ThumbnailItems;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.CallBackListener;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class ThumbnailRenderer extends ViewRenderer<ThumbnailModel, ThumbnailViewHolder> {

    private final Context mContext;
    @NonNull
    private CallBackListener mListener;

    public ThumbnailRenderer(Context context, CallBackListener listener) {
        super(ThumbnailModel.class);
        mContext = context;
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final ThumbnailModel model, @NonNull final ThumbnailViewHolder holder) {
        if (model.getMediaLargeIcon()!=null){
            Glide.with(mContext).load(model.getMediaLargeIcon()).into(holder.mIvThumbnail);
        }
        holder.mTvTitle.setText(model.mediaDescription());
        holder.mRrContainer.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mListener.onCategoryClicked(model);
            }
        });
    }

    @NonNull
    @Override
    public ThumbnailViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new ThumbnailViewHolder(inflate(R.layout.thumbnail_view_holder, parent));
    }
}
