package gala.com.kidstv.Refactored.Fragments;

import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;


import org.json.JSONException;

import java.util.ArrayList;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.DataHandlers.TabsModel;
import gala.com.kidstv.Refactored.DataHandlers.UTDataManager;
import gala.com.kidstv.Refactored.items.CircularItems.CircularRenderer;
import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;
import gala.com.kidstv.Refactored.items.Composite.Category.CategoryRenderer;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;
import gala.com.kidstv.Refactored.items.PagerItems.PagerRenderer;
import gala.com.kidstv.Refactored.items.PosterItems.PosterRenderer;
import gala.com.kidstv.Refactored.items.ThumbnailItems.ThumbnailRenderer;
import gala.com.kidstv.Refactored.items.widgetsUtils.BetweenSpacesItemDecoration;

public class GERecyclerFragment extends GEBaseFragment {

    private RendererRecyclerViewAdapter mRecyclerViewAdapter;
    private TabsModel                   mTabInfo;
    private ArrayList<CategoryModel>    mCategories;

    @Nullable
    private Bundle                      mSavedInstanceState;



    public GERecyclerFragment(ArrayList<CategoryModel> categoryList) {
         mCategories=categoryList;
    }

    @Nullable
    @Override
    public View onCreateView(final LayoutInflater inflater,
                             @Nullable final ViewGroup container,
                             @Nullable final Bundle savedInstanceState) {
        View inflate = inflater.inflate(R.layout.ge_recycler_fragment, container, false);

        mRecyclerViewAdapter = new RendererRecyclerViewAdapter();

        mRecyclerViewAdapter.registerRenderer(new CategoryRenderer()
                .registerRenderer(new MediaRenderer(null))
                .registerRenderer(new PagerRenderer(getActivity(), getDisplayMetrics()))
                .registerRenderer(new PosterRenderer(null))
                .registerRenderer(new CircularRenderer(null))
                .registerRenderer(new ThumbnailRenderer(null)));


        final RecyclerView recyclerView = (RecyclerView) inflate.findViewById(R.id.recycler_view);
        recyclerView.setAdapter(mRecyclerViewAdapter);
        recyclerView.addItemDecoration(new BetweenSpacesItemDecoration(10, 10));
        mRecyclerViewAdapter.setItems(mCategories);
        return inflate;
    }

    @Override
    public void onViewStateRestored(@Nullable final Bundle savedInstanceState) {
        super.onViewStateRestored(savedInstanceState);
        mSavedInstanceState = savedInstanceState;
    }

    @Override
    public void onResume() {
        super.onResume();
        mRecyclerViewAdapter.onRestoreInstanceState(mSavedInstanceState);
        mSavedInstanceState = null;
    }

    @Override
    public void onSaveInstanceState(@NonNull final Bundle outState) {
        super.onSaveInstanceState(outState);
        mRecyclerViewAdapter.onSaveInstanceState(outState);
    }

    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }
}
