package gala.com.urtube.model.GEMenu;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

/**
 * Created by deepak on 06/12/16.
 */

public class GEMenu
{
    private String                      mMenuId;
    private String                      mMenuName;
    private String                      mMenuCountry;
    private String                      mMenuCountryCode;
    private String                      mMenuImageIcon;
    private Boolean                     mIsSelected;
    private ArrayList<GESubMenu>        mSubMenus;

    GEMenu(JSONObject menuInfo)
    {
        System.out.println("GEMenu is created");
    }

    GEMenu(JSONObject menuInfo, JSONObject countryInfo)
    {
        mSubMenus = new ArrayList<GESubMenu>();
        try {
            mMenuId = menuInfo.getString("id");
            mMenuName = menuInfo.getString("name");
            mMenuImageIcon = menuInfo.getString("image");
            mMenuCountry = countryInfo.getString("name");
            mMenuCountryCode = countryInfo.getString("code");
            mIsSelected = false;

            JSONArray lSubMenus = menuInfo.getJSONArray("submenus");
            for (int index = 0; index < lSubMenus.length(); ++index)
            {
                JSONObject lSubMenuObj = lSubMenus.getJSONObject(index);
                GESubMenu lSubmenu = new GESubMenu(lSubMenuObj);
                mSubMenus.add(lSubmenu);
            }

        } catch (final JSONException e) {
            e.printStackTrace();
        }

        System.out.println("GEMenu is created");
    }

    public String getmMenuName()
    {
        if (mMenuName == null)
            return "No Name Available";

        return mMenuName;
    }

    public String getmMenuImageIcon()
    {
        if (mMenuImageIcon == null)
            return "No Name Available";

        return mMenuImageIcon;
    }

    public void setSelected(Boolean selected)
    {
        mIsSelected = selected;
    }

    public boolean isSelected()
    {
        return mIsSelected;
    }

    public ArrayList<GESubMenu> getmSubMenus() {
        return mSubMenus;
    }
}
