package gala.com.kidstv.Refactored.items.Composite;

import androidx.annotation.NonNull;

import com.github.vivchar.rendererrecyclerviewadapter.CompositeViewHolder;
import com.github.vivchar.rendererrecyclerviewadapter.CompositeViewState;

import java.io.Serializable;

import gala.com.kidstv.Refactored.items.Composite.Category.CategoryViewHolder;

public class GERecyclerViewState extends CompositeViewState<CategoryViewHolder> implements Serializable {

    public GERecyclerViewState(@NonNull final CompositeViewHolder holder) {
        super(holder);
    }

    @Override
    public void restore(@NonNull final CategoryViewHolder holder) {
        super.restore(holder);
    }
}