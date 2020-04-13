package gala.com.kidstv.Refactored.items.Composite;

import android.util.Log;
import android.view.ViewGroup;

import androidx.annotation.Nullable;

import com.github.vivchar.rendererrecyclerviewadapter.RendererRecyclerViewAdapter;
import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import java.util.List;

public class GENestedAdapter extends RendererRecyclerViewAdapter {

    private static final String TAG = GENestedAdapter.class.getSimpleName();

    public GENestedAdapter() {
        super();
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, final int typeIndex) {
        final ViewHolder viewHolder = super.onCreateViewHolder(parent, typeIndex);
        Log.d(TAG, "onCreateViewHolder: " + viewHolder.getClass().getSimpleName());
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position, @Nullable final List payloads) {
        Log.d(TAG, "onBindViewHolder: " + holder.getClass().getSimpleName());
        super.onBindViewHolder(holder, position, payloads);
    }

    @Override
    public void onViewRecycled(final ViewHolder holder) {
        Log.d(TAG, "onViewRecycled: " + holder.getClass().getSimpleName());
        super.onViewRecycled(holder);
    }
}
