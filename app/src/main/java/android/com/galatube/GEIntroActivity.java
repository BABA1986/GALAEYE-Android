package android.com.galatube;

import android.com.galatube.GETheme.GEThemeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.Window;
import android.view.WindowManager;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by deepak on 28/11/16.
 */

public class GEIntroActivity extends AppIntro
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        int lTextColor=GEThemeManager.getInstance(this).getSelectedNavTextColor();
        addSlide(AppIntroFragment.newInstance("Welcome!", "This is a demo of the AppIntro library.", R.drawable.ic_menu_gallery, lColor));
        addSlide(AppIntroFragment.newInstance("Clean App Intros", "This library offers developers the ability to add clean app intros at the start of their apps.", R.drawable.ic_menu_slideshow, lColor));
        addSlide(AppIntroFragment.newInstance("Simple, yet Customizable", "The library offers a lot of customization, while keeping it simple for those that like simple.", R.drawable.ic_menu_share, lColor));
        addSlide(AppIntroFragment.newInstance("Explore", "Feel free to explore the rest of the library demo!", R.drawable.ic_menu_slideshow, lColor));
//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
        setSeparatorColor(lTextColor);
        setBarColor(lColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
//        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);
//
//        // Turn vibration on and set intensity.
//        // NOTE: you will probably need to ask VIBRATE permission in Manifest.
        setVibrate(true);
        setVibrateIntensity(30);
    }
    @Override
    public void onSkipPressed(Fragment currentFragment){
        launchMainMenu();
    }

    @Override
    public void onDonePressed(Fragment fragment) {
        // Do something when users tap on Done button.
        launchMainMenu();
    }

    @Override
    public void onSlideChanged(@Nullable Fragment oldFragment, @Nullable Fragment newFragment) {
        // Do something when slide is changed
    }

    public void launchMainMenu()
    {
        Intent i = new Intent(GEIntroActivity.this, GEMainMenuActivity.class);
        startActivity(i);
    }
}

