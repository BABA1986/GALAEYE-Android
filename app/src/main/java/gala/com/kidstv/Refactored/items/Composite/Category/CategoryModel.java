package gala.com.kidstv.Refactored.items.Composite.Category;

import androidx.annotation.NonNull;

import com.github.vivchar.rendererrecyclerviewadapter.CompositeViewModel;
import com.github.vivchar.rendererrecyclerviewadapter.DefaultCompositeViewModel;

import java.util.ArrayList;
import java.util.HashMap;

import gala.com.kidstv.Refactored.DataHandlers.emums.CategoryTypeEnum;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;

public class CategoryModel extends DefaultCompositeViewModel {
    private String                       mCategoryId;
    private String                       mCategoryName;
    private String                       mCategoryDesc;
    private String                       mCategoryIcon;
    private CategoryTypeEnum             mCategoryType;
    private Number                       mCategoryIndex;
    private String                       mDataSource;
    private Number                       mID;

    private ArrayList<MediaModel>        mMediaList;

    public int getID() {
        return mID.intValue();
    }

    public CategoryModel(@NonNull HashMap<String, Object> categoryInfo,  ArrayList<MediaModel> mediaList)
    {
        super(mediaList);
        mCategoryId = (String)categoryInfo.get("mCategoryId");
        mID = (Number)categoryInfo.get("mId");
        mCategoryName = (String)categoryInfo.get("mCategoryName");
        mCategoryDesc = (String)categoryInfo.get("mCategoryDesc");
        mCategoryIcon = (String)categoryInfo.get("mCategoryIcon");
        Number lCategoryType = (Number)categoryInfo.get("mCategoryType");
        mCategoryType = new CategoryTypeEnum(lCategoryType.intValue());
        mDataSource = (String)categoryInfo.get("mDataSource");
        mCategoryIndex = (Number)categoryInfo.get("mCategoryIndex");

        mMediaList = mediaList;
    }

    public String categoryName() {
        return mCategoryName;
    }

    public String dataSource() {
        return mDataSource;
    }

    public ArrayList<MediaModel>mediaList() {
        return mMediaList;
    }

    public String categoryTypeString() {
        String lTypeStr = "Not Found";
        if(mCategoryType.categoryType == CategoryTypeEnum.ECategoryTypePager) {
            return "Pager Type Category";
        }
        else if(mCategoryType.categoryType == CategoryTypeEnum.ECategoryTypeMedia) {
            return "Media Type Category";
        }
        else if(mCategoryType.categoryType == CategoryTypeEnum.ECategoryTypeAdvertisement) {
            return "Google Ads Category";
        }
        else if(mCategoryType.categoryType == CategoryTypeEnum.ECategoryTypeAdvertisementURTube) {
            return "URTube Ads Category";
        }
        else {
            return lTypeStr;
        }
    }

    @Override
    public boolean equals(final Object o) {
        return o instanceof CompositeViewModel && mItems.equals(((CompositeViewModel) o).getItems());
    }

    @Override
    public int hashCode() {
        return mID.intValue();
    }

    @Override
    public String toString() {
        return getClass().getSimpleName() + "{" + mItems.toString() + "}";
    }
}
