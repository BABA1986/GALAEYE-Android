package gala.com.kidstv.Refactored.DataHandlers;

import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;

public class UTDataManager {

    private static UTDataManager    mDataManager;
    ArrayList<TabsModel>            mTabs;

    public static void init() {
        if (mDataManager == null) {
            mDataManager = new UTDataManager();
        }
    }

    public static UTDataManager getInstance() {
        init();
        return mDataManager;
    }

    public ArrayList<TabsModel> initTabs(Context context) throws JSONException {
        if(mTabs != null)
            return mTabs;

        mTabs = new ArrayList<TabsModel>();
        String lJSon = loadJsonFromAssets("URTubeAppData/index.json", context);
        JSONArray lTabsList = jsonListFormJsonString(lJSon);
        for(int i = 0; i < lTabsList.length(); ++i)
        {
            HashMap<String, Object> lTabInfo = new Gson().fromJson(lTabsList.get(i).toString(), HashMap.class);
            TabsModel lTab = new TabsModel(lTabInfo);
            mTabs.add(lTab);
        }

        return mTabs;
    }

    public ArrayList<CategoryModel> getCategoriesFor(TabsModel tabInfo, Context context) {
//        if(!("category").equalsIgnoreCase(tabInfo.mTabFor))
//            return null;

        if(tabInfo.tabCategories().size() > 0) return tabInfo.tabCategories();

        String lJSon = loadJsonFromAssets(tabInfo.categorySource(), context);
        JSONArray lCatList = jsonListFormJsonString(lJSon);
        Gson lConverter = new Gson();
        Type lType = new TypeToken<List<HashMap<String, Object>>>(){}.getType();
        ArrayList<HashMap<String, Object>> lCategories  =  lConverter.fromJson(String.valueOf(lCatList), lType );
        tabInfo.setCategories(lCategories, context);

        return tabInfo.tabCategories();
    }

    public ArrayList<HashMap<String, Object>> getMediaListFor(HashMap<String, Object> categoryModel, Context context) {

        String lJSon = loadJsonFromAssets((String) categoryModel.get("mDataSource"), context);
        JSONArray lMediaList = jsonListFormJsonString(lJSon);
        Gson lConverter = new Gson();
        Type lType = new TypeToken<List<HashMap<String, Object>>>(){}.getType();
        ArrayList<HashMap<String, Object>> lMedias  =  lConverter.fromJson(String.valueOf(lMediaList ), lType );
        return lMedias;
    }

    private String loadJsonFromAssets(String filePath, Context context) {
        String lJson = null;
        try {
            InputStream is = context.getAssets().open(filePath);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            lJson = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return lJson;
    }

    private JSONArray jsonListFormJsonString(String jsonStr) {
        JSONArray lJSONList = null;
        try {
            JSONObject lJSonObj = new JSONObject(jsonStr);
            lJSONList= (JSONArray)lJSonObj.getJSONArray("result");
        }catch (JSONException e) {
            e.printStackTrace();
        }

        return lJSONList;
    }
}
