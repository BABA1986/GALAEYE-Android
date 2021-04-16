package gala.com.kidstv.Refactored;

import androidx.annotation.NonNull;

import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;

public interface CallBackListener {
    void onCategoryClicked(@NonNull MediaModel model);
}
