package gala.com.urtube.GETheme;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by deepak on 06/03/17.
 */

public class GETheme
{
    private String                      mThemeID;
    private String                      mThemeName;
    private String                      mNavTextColor;
    private String                      mNavColor;

    GETheme(JSONObject themeInfo)
    {
        try {
            mThemeID = themeInfo.getString("id");
            mThemeName = themeInfo.getString("themename");
            mNavTextColor = themeInfo.getString("navtextcolor");
            mNavColor = themeInfo.getString("navcolor");
        } catch (final JSONException e) {
            e.printStackTrace();
        }

        System.out.println("GEMenu is created");
    }

    public String getmNavColor() {
        return mNavColor;
    }

    public String getmThemeName() {
        return mThemeName;
    }

    public String getmNavTextColor() {
        return mNavTextColor;
    }

    public String getmThemeID() {
        return mThemeID;
    }
}


