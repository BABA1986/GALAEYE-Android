package android.com.galatube.GEUserModal;

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

    public String getmUserImageUrl(){
        return mUserImageUrl;
    }
}
