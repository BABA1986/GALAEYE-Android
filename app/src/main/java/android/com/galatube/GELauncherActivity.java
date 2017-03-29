package android.com.galatube;

import android.app.Activity;
import android.com.galatube.model.GEMenu.GESharedMenu;
import android.content.Intent;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.content.SharedPreferences;

/**
 * Created by deepak on 30/11/16.
 */

public class GELauncherActivity extends Activity
{
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        GESharedMenu lSharedMenu = GESharedMenu.getInstance(getApplicationContext());

        Thread t = new Thread(      new Runnable() {
            @Override
            public void run() {
                //  Initialize SharedPreferences
                SharedPreferences getPrefs = PreferenceManager
                        .getDefaultSharedPreferences(getBaseContext());

                //  Create a new boolean and preference and set it to true
                boolean isFirstStart = getPrefs.getBoolean("firstStart", true);
                isFirstStart = true; //Temporary
                //  If the activity has never started before...
                if (isFirstStart)
                {
                    //  Launch app intro
                    Intent i = new Intent(GELauncherActivity.this, GEIntroActivity.class);
                    startActivity(i);

                    //  Make a new preferences editor
                    SharedPreferences.Editor e = getPrefs.edit();

                    //  Edit preference to make it false because we don't want this to run again
                    e.putBoolean("firstStart", false);

                    //  Apply changes
                    e.apply();
                }
            }
        });

        // Start the thread
        t.start();
    }
}
