package gala.com.kidstv.Refactored.items.Composite.Category;

import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vivchar.rendererrecyclerviewadapter.CompositeViewHolder;
import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class CategoryViewHolder extends CompositeViewHolder {
//
    public final LinearLayout mTitleBase;
    public final TextView mName;
    public final TextView mCategoryTypeDesc;
    public final View mViewAll;

    public CategoryViewHolder(final View itemView) {
        super(itemView);
        recyclerView = (RecyclerView) itemView.findViewById(R.id.recycler_view);

        mTitleBase = (LinearLayout) itemView.findViewById(R.id.title_base);
        mName = (TextView) itemView.findViewById(R.id.title);
        mCategoryTypeDesc = (TextView) itemView.findViewById(R.id.categorytype);
        mViewAll = itemView.findViewById(R.id.viewAll);
    }
}