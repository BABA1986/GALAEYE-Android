package android.com.galatube.GEUserModal;

import android.com.galatube.GEYoutubeEvents.GEEventListObj;
import android.com.galatube.GEYoutubeEvents.GEEventManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.google.android.gms.auth.api.signin.GoogleSignInAccount;

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
        mUserInfo.setmAuthToken("");
        mUserInfo.setmIdTokeen("");
        mUserInfo.setmRefreshToken("");
        mUserInfo.setmAccessToken("");
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

        String lAuthToken = sharedPreferences.getString("UserAuthToken","");
        mUserInfo.setmAuthToken(lAuthToken);

        String lIdToken = sharedPreferences.getString("UserIdToken","");
        mUserInfo.setmIdTokeen(lIdToken);

        String lRefreshToken = sharedPreferences.getString("RefreshToken","");
        mUserInfo.setmRefreshToken(lRefreshToken);

        String lAccessToken = sharedPreferences.getString("AccessToken","");
        mUserInfo.setmAccessToken(lAccessToken);
    }

    void saveState() {
        SharedPreferences lPreferences = mContext.getSharedPreferences("MyPreference", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = lPreferences.edit();
        editor.putString("UserName", mUserInfo.getUserName());
        editor.putString("UserId", mUserInfo.getmUserId());
        editor.putString("UserEmail", mUserInfo.getUserEmail());
        editor.putString("UserImageUrl", mUserInfo.getmUserImageUrl());
        editor.putString("UserAuthToken", mUserInfo.getmAuthToken());
        editor.putString("UserIdToken", mUserInfo.getmIdTokeen());
        editor.putString("RefreshToken", mUserInfo.getmRefreshToken());
        editor.putString("AccessToken", mUserInfo.getmAccessToken());
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

    public void setAuthToken(String authToken)
    {
        mUserInfo.setmAuthToken(authToken);
        saveState();
    }

    public void setIdToken(String idToken)
    {
        mUserInfo.setmIdTokeen(idToken);
        saveState();
    }

    public void setRefreshToken(String refreshToken)
    {
        mUserInfo.setmRefreshToken(refreshToken);
        saveState();
    }

    public void setAccessToken(String accessToken)
    {
        mUserInfo.setmAccessToken(accessToken);
        saveState();
    }
}
