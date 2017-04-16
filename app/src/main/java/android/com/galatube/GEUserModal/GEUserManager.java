package android.com.galatube.GEUserModal;

import android.com.galatube.GEYoutubeEvents.GEEventListObj;
import android.com.galatube.GEYoutubeEvents.GEEventManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import java.util.ArrayList;

/**
 * Created by RaviNITK on 02/03/17.
 */

public class GEUserManager
{
    private GEUserInfo              mUserInfo;
    static Context                  mContext;

    private static GEUserManager ourInstance = null;

    public static GEUserManager getInstance(Context context) {
        mContext = context;

        if (ourInstance == null)
            ourInstance = new GEUserManager(context);
        return ourInstance;
    }

    public void resetData(){
        SharedPreferences sharedPreferences=mContext.getSharedPreferences("MyPreference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPreferences.edit();
        editor.clear();
        editor.apply();
        editor.commit();

        mUserInfo.setUserName("");
        mUserInfo.setmUserId("");
        mUserInfo.setUserEmail("");
        mUserInfo.setmUserImageUrl("");
    }

    private GEUserManager(Context context) {
        initUserInfo();
    }

    public GEUserInfo getmUserInfo() {
        return mUserInfo;
    }

    void initUserInfo()
    {
        mUserInfo = new GEUserInfo();
        SharedPreferences sharedPreferences=mContext.getSharedPreferences("MyPreference",Context.MODE_PRIVATE);
        String lUserName = sharedPreferences.getString("UserName","");
        mUserInfo.setUserName(lUserName);

        String lUserId = sharedPreferences.getString("UserId","");
        mUserInfo.setmUserId(lUserId);

        String lEmail = sharedPreferences.getString("UserEmail","");
        mUserInfo.setUserEmail(lEmail);

        String lImageUrl = sharedPreferences.getString("UserImageUrl","");
        mUserInfo.setmUserImageUrl(lImageUrl);
    }

    void saveState()
    {
        SharedPreferences lPreferences = mContext.getSharedPreferences("MyPreference",Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=lPreferences.edit();
        editor.putString("UserName",mUserInfo.getUserName());
        editor.putString("UserId",mUserInfo.getmUserId());
        editor.putString("UserEmail",mUserInfo.getUserEmail());
        editor.putString("UserImageUrl",mUserInfo.getmUserImageUrl());
        editor.apply();
        editor.commit();
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
        mUserInfo.setUserEmail(email);
        saveState();
    }

    public void setUserImageUrl(String Url)
    {
        mUserInfo.setmUserImageUrl(Url);
        saveState();
    }
}
