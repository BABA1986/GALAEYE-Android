package gala.com.urtube;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
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
        addSlide(AppIntroFragment.newInstance("Your Favourite Comedians" + "\n" + "Under One Roof",  "Gear up for a laugh riot with URTube. We provide your favourite comedians videos under a one roof.", R.drawable.iconintro2, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Enjoy the Best Dramas",  "URTube is a great app for catching up on dramas you might not have heard of otherwise.", R.drawable.iconintro3, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Your Favorite Poets",  "India's largest poet platform here you can find all of your favorite poet videos.", R.drawable.iconintro4, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Do You Know?",  "A collection of popular mythological stories that brings to you stories of gods and goddesses, divine feats, tyranny, treachery, bravery, sacrifice, love and friendship.", R.drawable.iconintro5, lColor, lTextColor, lTextColor));
        addSlide(AppIntroFragment.newInstance("Motivational Speakers",  "To help you stay motivated, no matter what your job throws at you, we decided to compile more than 20 of the best motivational speeches from business, sports, entertainment, and more.", R.drawable.iconintro6, lColor, lTextColor, lTextColor));

//
//        // OPTIONAL METHODS
//        // Override bar/separator color.
        setImmersive(true);
        setImmersiveMode(true, true);
        setIndicatorColor(Color.parseColor("#FA0018"), lTextColor);
        Drawable lDrawable = new ColorDrawable(Color.TRANSPARENT);
//        setImageSkipButton(lDrawable);


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

