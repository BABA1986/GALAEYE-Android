package gala.com.kidstv.Refactored.items.PagerItems;

import android.content.Context;
import android.util.DisplayMetrics;
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
    private final DisplayMetrics mMetrics;
    private int itemMargin = 0;
    private int itemWidth = 0;

    public PagerRenderer(@NonNull final Context context, DisplayMetrics displayMetrics) {
        super(PagerModel.class);
        mContext = context;
        mMetrics = displayMetrics;
        setItemMargin(80);
        updateDisplayMetrics();
    }

    private void setItemMargin(int itemMargin) {
        this.itemMargin = itemMargin;
    }

    private void updateDisplayMetrics() {
        itemWidth = mMetrics.widthPixels - itemMargin * 2;
    }

    @Override
    public void bindView(@NonNull final PagerModel model, @NonNull final PagerViewHolder holder) {
        int currentItemWidth = itemWidth;
        if (holder.getAdapterPosition() == 0) {
            currentItemWidth += itemMargin;
            holder.itemView.setPadding(itemMargin, 0, 0, 0);
        } else if (holder.getAdapterPosition() == 5 - 1) {
            currentItemWidth += itemMargin;
            holder.itemView.setPadding(0, 0, itemMargin, 0);
        }
        int height = holder.mItemView.getLayoutParams().height;
        holder.mItemView. setLayoutParams(new ViewGroup.LayoutParams(currentItemWidth, height));

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
