package gala.com.kidstv.Refactored.items.Composite.Category;

import android.os.Build;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.PagerSnapHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vivchar.rendererrecyclerviewadapter.CompositeViewRenderer;
import com.github.vivchar.rendererrecyclerviewadapter.DefaultDiffCallback;
import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.ViewState;

import java.util.Collections;
import java.util.List;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.DataHandlers.emums.CategoryTypeEnum;
import gala.com.kidstv.Refactored.items.Composite.BetweenSpacesItemDecoration;
import gala.com.kidstv.Refactored.items.Composite.GENestedAdapter;
import gala.com.kidstv.Refactored.items.Composite.GERecyclerViewState;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class CategoryRenderer extends CompositeViewRenderer<CategoryModel, CategoryViewHolder> {

    private static final String TAG = CategoryRenderer.class.getSimpleName();

    public CategoryRenderer() {
        super(CategoryModel.class);
    }

    @Override
    public void rebindView(@NonNull final CategoryModel model, @NonNull final CategoryViewHolder holder, @NonNull final List<Object> payloads) {
        Log.d(TAG, "rebindView " + model.toString() + ", payload: " + payloads.toString());
        holder.getAdapter().enableDiffUtil();
        holder.getAdapter().setItems(model.getItems());
        holder.getAdapter().notifyDataSetChanged();
    }

    @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
    @Override
    public void bindView(@NonNull final CategoryModel model, @NonNull final CategoryViewHolder holder) {
        Log.d(TAG, "bindView " + model.toString());
        RendererRecyclerViewAdapter lAdapter = holder.getAdapter();
        lAdapter.disableDiffUtil();
        lAdapter.setItems(model.getItems());
        lAdapter.notifyDataSetChanged();
        holder.mName.setText(model.categoryName());
        holder.mCategoryTypeDesc.setText(model.categoryTypeString());

        if(model.categoryType().categoryType == CategoryTypeEnum.ECategoryTypePager) {
            holder.mTitleBase.setVisibility(View.GONE);
            holder.recyclerView.setClipToPadding(false);
            holder.recyclerView.setPaddingRelative(12,0,12,0);
            new PagerSnapHelper().attachToRecyclerView(holder.recyclerView);
        }

//		holder.getRecyclerView().addOnScrollListener(new EndlessScrollListener() {
//			@Override
//			public void onLoadMore(int page, int totalItemsCount) {
////				holder.getAdapter().showLoadMore();
//				/* sendLoadMoreRequest(model.getID()); */
//				Log.d(TAG, "load more called" + model.toString());
//			}
//		});
    }

    @NonNull
    @Override
    public CategoryViewHolder createCompositeViewHolder(@Nullable final ViewGroup parent) {
        return new CategoryViewHolder(inflate(R.layout.category_view_holder, parent));
    }

    @Nullable
    @Override
    public ViewState createViewState(@NonNull final CategoryViewHolder holder) {
        return new GERecyclerViewState(holder);
    }

    @Override
    public int createViewStateID(@NonNull final CategoryModel model) {
        return model.getID();
    }

    @NonNull
    @Override
    protected RendererRecyclerViewAdapter createAdapter() {
        final GENestedAdapter nestedAdapter = new GENestedAdapter();
        nestedAdapter.setDiffCallback(new DefaultDiffCallback());
        return nestedAdapter;
    }

    @NonNull
    @Override
    protected List<? extends RecyclerView.ItemDecoration> createItemDecorations() {
        return Collections.singletonList(new BetweenSpacesItemDecoration(0, 10));
    }
}