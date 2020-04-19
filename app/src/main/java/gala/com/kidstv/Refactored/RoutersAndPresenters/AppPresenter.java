package gala.com.kidstv.Refactored.RoutersAndPresenters;

import androidx.annotation.NonNull;

import java.util.ArrayList;

import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;

public class AppPresenter extends BasePresenter {

    @NonNull
    private final AppRouting mUIRouter;

    public AppPresenter(@NonNull final AppRouting UIRouter, final boolean firstInit) {
        mUIRouter = UIRouter;
    }

    public void loadFragment(ArrayList<CategoryModel> categoryList){
        mUIRouter.openHomePage(categoryList);
    }
}
