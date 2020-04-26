package gala.com.kidstv.Refactored.items.CircularItems;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;
import gala.com.kidstv.Refactored.items.PagerItems.PagerModel;
import gala.com.kidstv.Refactored.items.PagerItems.PagerViewHolder;

public class CircularRenderer extends ViewRenderer<CircularModel, CircularViewHolder> {

    @NonNull
    private final MediaRenderer.Listener mListener;

    public CircularRenderer(@NonNull final MediaRenderer.Listener listener) {
        super(CircularModel.class);
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final CircularModel model, @NonNull final CircularViewHolder holder) {
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
