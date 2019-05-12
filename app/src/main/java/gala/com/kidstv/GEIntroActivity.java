package gala.com.kidstv;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;

import com.github.paolorotolo.appintro.AppIntro2;
import com.github.paolorotolo.appintro.AppIntroFragment;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by deepak on 28/11/16.
 */

public class GEIntroActivity extends AppIntro2
{
    private boolean         mDissmissOnDoneAndSkip;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Intent intent = getIntent();
        mDissmissOnDoneAndSkip = intent.getExtras().getBoolean("Dismiss");
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        int lColor = Color.TRANSPARENT;
        int lTextColor = Color.DKGRAY;
        addSlide(AppIntroFragment.newInstance("Watch your favorite hero's Videos",  "This is the plateform here you can watch the video of more than 100 cartoon heros ", R.drawable.iconintro1, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Grandma Stories",  "We present you a collection of best Grandma Stories. All, mostly present in the bedtime stories and afternoon later, are the ones told by our Grandparents.", R.drawable.iconintro2, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Learn Crafting",  "Crafts are a great way to bond with your children, foster them development and open doors for learning new skills.", R.drawable.iconintro3, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Engaging Video Lessons",  "Videos that help you visualize each concept, making it easier to understand. Clearer concepts lead to higher scores!", R.drawable.iconintro4, lColor, lTextColor, lTextColor));

        addSlide(AppIntroFragment.newInstance("Safe Search Kids",  "We are happy to offer a video filtering." + "\n" +  "Kids TV gauranteed, Your kids is watching the safe videos.", R.drawable.iconintro5, lColor, lTextColor, lTextColor));

//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
        showStatusBar(false);
        setNavBarColor("#2D2D2D");
        setImmersive(true);
        setImmersiveMode(true, true);
        setIndicatorColor(Color.parseColor("#FA0018"), lTextColor);
        Drawable lDrawable = new ColorDrawable(Color.TRANSPARENT);



//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            Window window = getWindow();
//            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
//            window.setStatusBarColor(Color.TRANSPARENT);
//        }
//        // Hide Skip/Done button.
        showSkipButton(true);
        Drawable lSkipBtn = getResources().getDrawable( R.drawable.skip );
        setImageSkipButton(lSkipBtn);

        setProgressButtonEnabled(true);



        setVibrate(true);
        setVibrateIntensity(30);
        ImageView imageView = new ImageView(this);
        imageView.setImageResource(R.drawable.introbck3);
        imageView.setScaleType(ImageView.ScaleType.FIT_XY);
        imageView.setBackgroundColor(Color.WHITE);
        imageView.setLayoutParams(new FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        setBackgroundView(imageView);

    }
    @Override
    public void onSkipPressed(Fragment currentFragment){
        if (mDissmissOnDoneAndSkip)
            finish();
        else
            launchMainMenu();
    }

    @Override
    public void onDonePressed(Fragment fragment) {
        // Do something when users tap on Done button.
        if (mDissmissOnDoneAndSkip)
            finish();
        else
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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}

