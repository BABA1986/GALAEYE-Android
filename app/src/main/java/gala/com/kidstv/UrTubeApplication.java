package gala.com.kidstv;

import android.app.Application;
import android.content.Context;
import uk.co.chrisjenx.calligraphy.CalligraphyConfig;

public class UrTubeApplication extends Application {

    public static Context mAppContext;

    @Override
    public void onCreate() {
        super.onCreate();

        CalligraphyConfig.initDefault(new CalligraphyConfig.Builder()
                .setDefaultFontPath("fonts/Muli-Regular.ttf")
                .setFontAttrId(R.attr.fontPath)
                .build()
        );

        mAppContext = getApplicationContext();
    }

    public static UrTubeApplication get(Context context) {
        return (UrTubeApplication) context.getApplicationContext();
    }
}
