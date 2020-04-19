package gala.com.kidstv.Refactored.RoutersAndPresenters;

import android.app.Activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import java.util.ArrayList;

import gala.com.kidstv.Refactored.DataHandlers.TabsModel;
import gala.com.kidstv.Refactored.Fragments.GEBaseFragment;
import gala.com.kidstv.Refactored.Fragments.GERecyclerFragment;
import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;

public class AppRouting {
    @NonNull
    private final Activity mContext;
    @NonNull
    private final FragmentManager mFragmentManager;

    public AppRouting(@NonNull final AppCompatActivity activity) {
        mContext = activity;
        mFragmentManager = activity.getSupportFragmentManager();
    }

    @NonNull
    public Activity getContext() {
        return mContext;
    }

    @NonNull
    public FragmentManager getFragmentManager() {
        return mFragmentManager;
    }

    public void openHomePage(ArrayList<CategoryModel> categoryList) {
        showFragment(new GERecyclerFragment(categoryList));
    }

    private void showFragment(@NonNull final GEBaseFragment fragment) {
        try {
            getFragmentManager().beginTransaction()
                    .replace(R.id.replace_content, fragment, fragment.getClass().getName())
                    .addToBackStack(null)
                    .commitAllowingStateLoss();
        } catch (IllegalStateException ignored) {
        }
    }
}
