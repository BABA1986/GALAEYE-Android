package gala.com.kidstv.Refactored.DataHandlers;

import android.content.Context;

import androidx.annotation.NonNull;

import java.util.ArrayList;
import java.util.HashMap;

import gala.com.kidstv.Refactored.DataHandlers.emums.CategoryTypeEnum;
import gala.com.kidstv.Refactored.DataHandlers.emums.MediaTypeEnum;
import gala.com.kidstv.Refactored.items.CircularItems.CircularModel;
import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.PagerItems.PagerModel;
import gala.com.kidstv.Refactored.items.PosterItems.PosterModel;
import gala.com.kidstv.Refactored.items.ThumbnailItems.ThumbnailModel;

public class TabsModel {

    public ArrayList<CategoryModel> getmCatagories() {
        return mCatagories;
    }

    private Number                      mTabId;
    private String                      mTabName;
    private String                      mDataSource;
    public String                       mTabFor;
    private String                      mTabIcon;
    private ArrayList<CategoryModel>    mCatagories;

    public TabsModel(@NonNull HashMap<String, Object> tabInfo)
    {
        mTabId = (Number)tabInfo.get("mTabId");
        mTabName = (String)tabInfo.get("mTabName");
        mDataSource = (String)tabInfo.get("mDataSource");
        mTabFor = (String)tabInfo.get("mTabType");
        mTabIcon = (String)tabInfo.get("mTabIcon");

        mCatagories = new ArrayList<CategoryModel>();
    }

    public String categorySource() {
        return mDataSource;
    }

    public void setCategories(ArrayList<HashMap<String, Object>> categories, Context context) {
        for(HashMap<String, Object> lCategoryInfo: categories) {
            ArrayList<HashMap<String, Object>> lMediaList = UTDataManager.getInstance().getMediaListFor(lCategoryInfo, context);
            ArrayList<MediaModel> lMediaObjs = new ArrayList<MediaModel>();

            Number lCatTypeEnum = (Number)lCategoryInfo.get("mCategoryType");
            CategoryTypeEnum lCategoryType = new CategoryTypeEnum(lCatTypeEnum.intValue());

            for(HashMap<String, Object> lMediaInfo: lMediaList) {
                lMediaObjs.add(modelFor(lCategoryType, lMediaInfo));
            }
            CategoryModel lCategory = new CategoryModel(lCategoryInfo, lMediaObjs);
            mCatagories.add(lCategory);
        }
    }

    private MediaModel modelFor(CategoryTypeEnum categoryType, HashMap<String, Object> lMediaInfo) {
        if(categoryType.categoryType == CategoryTypeEnum.ECategoryTypePosters)
        {
            PosterModel lPosterModel = new PosterModel(lMediaInfo);
            return lPosterModel;
        }
        else if(categoryType.categoryType == CategoryTypeEnum.ECategoryTypePager)
        {
            PagerModel lPagerModel = new PagerModel(lMediaInfo);
            return lPagerModel;
        }
        else if(categoryType.categoryType == CategoryTypeEnum.ECategoryTypeThumbnail)
        {
            ThumbnailModel lThumbnailModel = new ThumbnailModel(lMediaInfo);
            return lThumbnailModel;
        }
        else if(categoryType.categoryType == CategoryTypeEnum.ECategoryTypeCircular
        || categoryType.categoryType == CategoryTypeEnum.ECategoryTypeCircularText)
        {
            CircularModel lCircularModel = new CircularModel(lMediaInfo);
            return lCircularModel;
        }

        MediaModel lMedia = new MediaModel(lMediaInfo);
        return lMedia;
    }

    public ArrayList<CategoryModel> tabCategories() {
        return mCatagories;
    }

    public Number getmTabId() {
        return mTabId;
    }

    public String getmTabName() {
        return mTabName;
    }

    public String getmDataSource() {
        return mDataSource;
    }

    public String getmTabFor() {
        return mTabFor;
    }

    public String getmTabIcon() {
        return mTabIcon;
    }
}
