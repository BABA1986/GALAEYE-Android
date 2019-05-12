package gala.com.kidstv.firebase;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
/**
 * Created by kiwitech on 2/1/18.
 */

public class FirebaseRegistrationBroadcastReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        String token = intent.getStringExtra("token");
        if(token != null){
            //send token to server here
            return;
        }
       // String message = intent.getStringExtra(AppConstants.IntentConstants.MESSAGE);
        //Toast.makeText(context, message, Toast.LENGTH_LONG).show();
    }
}
