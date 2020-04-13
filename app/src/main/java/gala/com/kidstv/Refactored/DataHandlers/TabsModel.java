package gala.com.kidstv.Refactored.DataHandlers;

import android.content.Context;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;

public class TabsModel {
    private Number                      mTabId;
    private String                      mTabName;
    private String                      mDataSource;
    public String                      mTabFor;
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
            for(HashMap<String, Object> lMediaInfo: lMediaList) {
                MediaModel lMedia = new MediaModel(lMediaInfo);
                lMediaObjs.add(lMedia);
            }
            CategoryModel lCategory = new CategoryModel(lCategoryInfo, lMediaObjs);
            mCatagories.add(lCategory);
        }
    }

    public ArrayList<CategoryModel> tabCategories() {
        return mCatagories;
    }
}
