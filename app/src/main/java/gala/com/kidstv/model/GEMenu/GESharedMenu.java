package gala.com.kidstv.model.GEMenu;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by deepak on 06/12/16.
 */
public class GESharedMenu
{
    private ArrayList<GEMenu>              mMenus;
    private Context                        mContext;
    private static GESharedMenu            ourInstance;

    public static GESharedMenu getInstance(Context context)
    {
        if (ourInstance == null) {
            ourInstance = new GESharedMenu();
            ourInstance.mContext = context;
            ourInstance.initialiseMenuItems();
        }
        return ourInstance;
    }

    public ArrayList<GEMenu> getMenus()
    {
        return mMenus;
    }

    public void initialiseMenuItems()
    {
        mMenus = new ArrayList<GEMenu>();
        String lJson = jSonString();
        try {
            JSONObject lRootJsonObject = new JSONObject(lJson);
            JSONArray lCountries = lRootJsonObject.getJSONArray("countries");
            for (int i = 0; i < lCountries.length(); ++i)
            {
                JSONObject lCountryObj =  lCountries.getJSONObject(i);
                JSONArray lMenues = lCountryObj.getJSONArray("menus");
                for (int index = 0; index < lMenues.length(); ++index)
                {
                    JSONObject lMenuObj = lMenues.getJSONObject(index);
                    GEMenu lMenuItem = new GEMenu(lMenuObj, lCountryObj);
                    mMenus.add(lMenuItem);
                }
            }
        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    public String jSonString()
    {
        String lJSON = null;
        try {
            InputStream lInputStream = mContext.getAssets().open("GalaMenuMetaData.json");
            int size = lInputStream.available();
            byte[] lBuffer = new byte[size];
            lInputStream.read(lBuffer);
            lInputStream.close();
            lJSON = new String(lBuffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }

        return lJSON;
    }
}
