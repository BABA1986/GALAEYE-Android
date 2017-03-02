package android.com.galatube.GEUserModal;

import android.com.galatube.GEYoutubeEvents.GEEventListObj;
import android.com.galatube.GEYoutubeEvents.GEEventManager;

import java.util.ArrayList;

/**
 * Created by RaviNITK on 02/03/17.
 */

public class GEUserManager
{
    GEUserInfo              mUserInfo;

    private static GEUserManager ourInstance = new GEUserManager();

    public static GEUserManager getInstance() {
        return ourInstance;
    }

    private GEUserManager() {
        mUserInfo = new GEUserInfo();
    }

    void saveState()
    {

    }

    public void setUserName(String name)
    {
        mUserInfo.setUserName(name);
        saveState();
    }

    public void setUserId(String userId)
    {
        mUserInfo.setmUserId(userId);
        saveState();
    }

    public void setUserEmail(String email)
    {
        mUserInfo.setmUserId(email);
        saveState();
    }

    public void setUserImageUrl(String Url)
    {
        mUserInfo.setmUserId(Url);
        saveState();
    }
}
