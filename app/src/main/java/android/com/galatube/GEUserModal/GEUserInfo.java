package android.com.galatube.GEUserModal;

import static android.R.attr.id;
import static android.R.attr.name;
import static android.R.attr.thickness;

/**
 * Created by RaviNITK on 02/03/17.
 */

public class GEUserInfo
{
     private  String mUserName;
     private  String mUserId;
     private  String mUserEmail;
     private  String mUserImageUrl;
     private  String mAuthToken;
     private  String mIdTokeen;
     private  String mRefreshToken;
     private  String mAccessToken;
     private  String mTokenExpireAt;

    public void setUserName(String userName)
    {
        this.mUserName = userName;
    }

    public String getUserName()
    {
        return mUserName;
    }

    public void setmUserId(String userID)
    {
        this.mUserId = userID;
    }

    public void setmAuthToken(String authToken)
    {
        this.mAuthToken = authToken;
    }

    public void setmIdTokeen(String idTokeen)
    {
        this.mIdTokeen = idTokeen;
    }

    public String getmUserId()
    {
        return mUserId;
    }

    public void setUserEmail(String email)
    {
        this.mUserEmail = email;
    }

    public String getUserEmail(){
        return mUserEmail;
    }

    public void setmUserImageUrl(String url){
        this.mUserImageUrl=url;
    }

    public void setmRefreshToken(String refreshToken)
    {
        this.mRefreshToken = refreshToken;
    }

    public void setmAccessToken(String accessToken)
    {
        this.mAccessToken = accessToken;
    }

    public String getmUserImageUrl(){
        return mUserImageUrl;
    }

    public String getmAuthToken(){
        return mAuthToken;
    }

    public String getmIdTokeen(){
        return mIdTokeen;
    }

    public String getmRefreshToken(){
        return mRefreshToken;
    }

    public String getmAccessToken(){
        return mAccessToken;
    }
}
