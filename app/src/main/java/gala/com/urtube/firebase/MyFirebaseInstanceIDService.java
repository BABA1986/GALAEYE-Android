package gala.com.urtube.firebase;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

import java.util.HashMap;

public class MyFirebaseInstanceIDService extends FirebaseInstanceIdService {

    private static final String TAG = MyFirebaseInstanceIDService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();

        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i("regId", refreshedToken);
        // Saving reg id to shared preferences
        storeRegIdInPref(refreshedToken);

        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference("users");
        HashMap<String, String> lUserInfo = new HashMap<String, String>();
        lUserInfo.put("name", "Not Yet Login");
        lUserInfo.put("email", "Not Yet Login");
        lUserInfo.put("imageurl", "Not Yet Login");
        lUserInfo.put("token", refreshedToken);
        mDatabase.child(refreshedToken).setValue(lUserInfo);

        // Registration has completed, send token to server.
        Intent registrationComplete = new Intent(NotificationConfig.REGISTRATION_COMPLETE);
        registrationComplete.putExtra("token", refreshedToken);
        //registrationComplete.setAction(AppConstants.IntentFilterConstants.FilterPushNotification);
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete);
    }

    private void storeRegIdInPref(String token) {
//        AppPreference preference = MyCircleApplication.get(MyCircleApplication.mAppContext).getComponent().prefs();
//        preference.saveFcmToken(token);
        SharedPreferences pref = getApplicationContext().getSharedPreferences(NotificationConfig.SHARED_PREF, 0);
        SharedPreferences.Editor editor = pref.edit();
        //editor.putString(AppConstants.FCM_TOKEN, token);
        editor.commit();
    }
}

