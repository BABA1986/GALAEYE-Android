package gala.com.kidstv.Refactored.items.PagerItems;

import android.content.Context;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.FragmentActivity;

import com.bumptech.glide.Glide;
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;


public class PagerRenderer extends ViewRenderer<PagerModel, PagerViewHolder> {

    private final Context mContext;

    public PagerRenderer(@NonNull final Context context) {
        super(PagerModel.class);
        mContext = context;
    }

    @Override
    public void bindView(@NonNull final PagerModel model, @NonNull final PagerViewHolder holder) {
        holder.mTvCount.setText(holder.getAdapterPosition()+1+"/5");
        if (model.getMediaLargeIcon()!=null){
            Glide.with(mContext).load(model.getMediaLargeIcon()).into(holder.mIvMedia);
        }
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
