package android.com.galatube;

import android.com.galatube.GETheme.GEThemeManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBar;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.AppIntro;
import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntro2Fragment;
import com.github.paolorotolo.appintro.AppIntroFragment;

/**
 * Created by deepak on 28/11/16.
 */

public class GEIntroActivity extends AppIntro2
{
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        int lColor = Color.TRANSPARENT;
        int lTextColor = Color.DKGRAY;
        addSlide(AppIntroFragment.newInstance("A place to \n" + "watch live events",  "Watch live top concert events streaming on your mobile phones.", R.drawable.iconintro1, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("All in One \n" + "your favorite commedians",  "More than 50 commedy channels on single place with more than 10000 videos. Provides all the updated video for you.", R.drawable.iconintro2, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("All in One \n" + "your favorite dramas",  "More than 50 drama short films channels from defferent plateforms with more than 50000 videos. It provides you the latest video as soon as they published by any of the channel.", R.drawable.iconintro3, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Your favorite poets",  "Indias largest poet platform here you can find all of your favorite poet videos. It provides you the latest video as soon as they published by any of the channel.", R.drawable.iconintro4, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("What do you know about Epic?",  "Application provides airs action, drama, comedy and narrative non-fiction and fictional programming with a focus on Indian history, folklore and mythology genre.", R.drawable.iconintro5, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Motivational Speaker",  "To help you stay motivated, no matter what your job throws at you, we decided to compile 22 of the best motivational speeches from business, sports, entertainment, and more. If you want to get fired up for a project, watch these videos. Trust me, I was ready to write a 5,000 word blog post after I saw them.", R.drawable.iconintro6, lColor, lTextColor, lTextColor));

//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
//        setImmersive(true);
//        setImmersiveMode(true, true);
        setIndicatorColor(Color.parseColor("#FA0018"), lTextColor);
        Drawable lDrawable = new ColorDrawable(Color.TRANSPARENT);
        setImageSkipButton(lDrawable);


//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//        // Hide Skip/Done button.
        showSkipButton(true);
        setProgressButtonEnabled(true);

        setVibrate(true);
        setVibrateIntensity(30);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.introbck3);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundView(imageView);
    }
    @Override
    public void onSkipPressed(Fragment currentFragment){

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

