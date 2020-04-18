package gala.com.kidstv.Refactored.RoutersAndPresenters;

import androidx.annotation.NonNull;

public class AppPresenter extends BasePresenter {

    @NonNull
    private final AppRouting mUIRouter;

    public AppPresenter(@NonNull final AppRouting UIRouter, final boolean firstInit) {
        mUIRouter = UIRouter;
        if (firstInit) {
            mUIRouter.openHomePage();
        }
    }

    public void loadFragment(){
        mUIRouter.openHomePage();
    }
}
