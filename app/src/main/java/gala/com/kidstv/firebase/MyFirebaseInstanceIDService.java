package gala.com.kidstv.firebase;

import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

import gala.com.kidstv.GEUserModal.GEUserManager;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("regId", refreshedToken);
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        HashMap<String, String> lUserInfo = new HashMap<String, String>();
        GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
        String lUserName = lGEUserManager.getmUserInfo().getUserName();
        String lUserEmail = lGEUserManager.getmUserInfo().getUserEmail();
        lUserInfo.put("name", lUserName);
        lUserInfo.put("email", lUserEmail);
        lUserInfo.put("token", refreshedToken);

        mDatabase.child(refreshedToken).setValue(lUserInfo);
    }
}

