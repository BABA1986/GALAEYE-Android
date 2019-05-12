package gala.com.kidstv.kidsLauncher;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import gala.com.kidstv.R;

/**
 * Created by deepak on 29/04/19.
 */

public class GEKidsLauncherActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_kidstv_launcher);

        TextView lKText = (TextView) findViewById(R.id.ktext);
        lKText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));

        TextView lIText = (TextView) findViewById(R.id.itext);
        lIText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));

        TextView lDText = (TextView) findViewById(R.id.dtext);
        lDText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));

        TextView lSText = (TextView) findViewById(R.id.stext);
        lSText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));

        TextView lTVText = (TextView) findViewById(R.id.tvtext);
        lTVText.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));

        TextView lPoweredeBy = (TextView) findViewById(R.id.poweredby);
        lPoweredeBy.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/Muli-Regular.ttf"));

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

//        applyTheme();
        startanimation();
    }

    public void startanimation() {
        ImageView lWhiteCircle = (ImageView) findViewById(R.id.icwhitecircle);
        ScaleAnimation lScaleAnimation = new ScaleAnimation((float) 0.0, (float) 1.0, (float) 0.0, (float) 1.0, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        lScaleAnimation.setFillAfter(true);
        lScaleAnimation.setDuration(500);
        lWhiteCircle.setAnimation(lScaleAnimation);
        lWhiteCircle.startAnimation(lScaleAnimation);
        lScaleAnimation.setAnimationListener(new Animation.AnimationListener() {
            public void onAnimationStart(Animation anim) {

            }

            public void onAnimationRepeat(Animation anim) {

            }

            public void onAnimationEnd(Animation anim) {
                kidsCharacterAnimation();
            }
        });
    }

    public void kidsCharacterAnimation() {
        TextView lTVText = (TextView) findViewById(R.id.tvtext);
        Animation lTVAnimation = AnimationUtils.loadAnimation(getApplicationContext(), R.anim.text_rotate_bounce);
        lTVText.startAnimation(lTVAnimation);

        TextView lSText = (TextView) findViewById(R.id.stext);
        lTVText.startAnimation(lTVAnimation);
    }
}
