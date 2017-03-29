package android.com.galatube.GETheme;

import android.com.galatube.model.GEMenu.GEMenu;
import android.content.Context;
import android.graphics.Color;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by deepak on 06/03/17.
 */

public class GEThemeManager
{
    private Context                        mContext;
    private int                            mSelectedIndex;

    private static GEThemeManager ourInstance = new GEThemeManager();

    public static GEThemeManager getInstance(Context context) {
        ourInstance.mContext = context;
        ourInstance.initialiseThemes();
        return ourInstance;
    }

    private ArrayList<GETheme> mThemes;

    private GEThemeManager()
    {

    }

    public void setmSelectedIndex(int selectedIndex) {

        mSelectedIndex = selectedIndex;
    }

    public void initialiseThemes()
    {
        mThemes = new ArrayList<GETheme>();
        String lJson = jSonString();
        try {
            JSONObject lRootJsonObject = new JSONObject(lJson);
            JSONArray lThemes = lRootJsonObject.getJSONArray("Themes");
            for (int index = 0; index < lThemes.length(); ++index)
            {
                JSONObject lThemeInfo = lThemes.getJSONObject(index);
                GETheme lTheme = new GETheme(lThemeInfo);
                mThemes.add(lTheme);
            }

        } catch (final JSONException e) {
            e.printStackTrace();
        }
    }

    public String jSonString()
    {
        String lJSON = null;
        try {
            InputStream lInputStream = mContext.getAssets().open("GalaThemeData.json");
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

    public int getSelectedNavColor()
    {
        GETheme lThemeInfo = mThemes.get(mSelectedIndex);
        String lColorStr = lThemeInfo.getmNavColor();
        return Color.parseColor(lColorStr);
    }

    public int getSelectedNavTextColor()
    {
        GETheme lThemeInfo = mThemes.get(mSelectedIndex);
        String lColorStr = lThemeInfo.getmNavTextColor();
        return Color.parseColor(lColorStr);
    }
}
