package gala.com.urtube.model.GEMenu;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by deepak on 06/12/16.
 */

public class GESubMenu
{
    private String                      mSubMenuId;
    private String                      mSubMenuSrc;
    private String                      mSubMenuName;
    private String                      mSubMenuType;
    private boolean                     mIsChannelId;

    GESubMenu(JSONObject subMenuInfo)
    {
        try {
            mSubMenuId = subMenuInfo.getString("id");
            mSubMenuSrc = subMenuInfo.getString("src");
            mSubMenuName = subMenuInfo.getString("name");
            mSubMenuType = subMenuInfo.getString("type");
            mIsChannelId = subMenuInfo.getBoolean("issourceid");
        } catch (final JSONException e) {
            e.printStackTrace();
        }
        System.out.println("GESubMenu is created");
    }

    public String getmSubMenuName() {
        return mSubMenuName;
    }

    public String getmSubMenuSrc() {

        return mSubMenuSrc;
    }

    public String getmSubMenuType() {

        return mSubMenuType;
    }

    public boolean ismIsChannelId() {
        return mIsChannelId;
    }
}