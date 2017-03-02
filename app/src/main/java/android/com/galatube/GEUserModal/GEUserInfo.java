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

    public String getUserName(String UserName)
    {
        this.mUserName=UserName;
        return mUserName;
    }

    public String getmUserId(String userId){
        this.mUserId=userId;
        return mUserId;
    }


    public String getUserEmail(String email){
        this.mUserEmail=email;
        return mUserEmail;
    }


    public String getmUserImageUrl(String url){
        this.mUserImageUrl=url;
        return mUserImageUrl;
    }
}
