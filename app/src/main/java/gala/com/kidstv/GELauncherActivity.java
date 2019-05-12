package gala.com.kidstv;

import android.app.Activity;
import gala.com.kidstv.GETheme.GEThemeManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.content.SharedPreferences;
import android.support.v4.app.ActivityOptionsCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deepak on 30/11/16.
 */

public class GELauncherActivity extends Activity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_launcher);

        View decorView = getWindow().getDecorView();
        decorView.setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                        | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                        | View.SYSTEM_UI_FLAG_IMMERSIVE);

        applyTheme();
        startanimation();
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("myTheme", MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition", 0));
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
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
                ImageView lIcTubeRed = (ImageView) findViewById(R.id.ictubered);
                lIcTubeRed.setVisibility(View.VISIBLE);
                TranslateAnimation lTranslateAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, -1.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
                lTranslateAnim.setFillAfter(true);
                lTranslateAnim.setDuration(500);
                lIcTubeRed.setAnimation(lTranslateAnim);
                lIcTubeRed.startAnimation(lTranslateAnim);
                lTranslateAnim.setAnimationListener(new Animation.AnimationListener() {
                    public void onAnimationStart(Animation anim) {

                    }

                    public void onAnimationRepeat(Animation anim) {

                    }

                    public void onAnimationEnd(Animation anim) {
                        TranslateAnimation lUrTranslateAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, -1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
                        lUrTranslateAnim.setFillAfter(true);
                        lUrTranslateAnim.setDuration(500);
                        ImageView lIcUrRed = (ImageView) findViewById(R.id.icured);
                        lIcUrRed.setAnimation(lUrTranslateAnim);
                        lIcUrRed.startAnimation(lUrTranslateAnim);
                        lUrTranslateAnim.setAnimationListener(new Animation.AnimationListener() {
                            public void onAnimationStart(Animation anim) {

                            }

                            public void onAnimationRepeat(Animation anim) {

                            }

                            public void onAnimationEnd(Animation anim) {

                                new Timer().schedule(new TimerTask() {
                                    @Override
                                    public void run() {
                                        SharedPreferences lPreferences = getSharedPreferences("MyPreference", Context.MODE_PRIVATE);
                                        SharedPreferences.Editor editor = lPreferences.edit();
                                        int lLaunchCount = lPreferences.getInt("LaunchCount", 1);
                                        if (lLaunchCount == 1) {
                                            editor.putInt("LaunchCount", 2);
                                            editor.apply();
                                            editor.commit();
                                        }

                                        Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                                        if (lLaunchCount == 1) {
                                            Intent i = new Intent(GELauncherActivity.this, GEIntroActivity.class);
                                            i.putExtra("Dismiss", false);
                                            startActivity(i, bundle);
                                            finish();
                                        }
                                        else {
                                            Intent i = new Intent(GELauncherActivity.this, GEMainMenuActivity.class);
                                            startActivity(i);
                                            finish();
                                        }
                                        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                    }
                                }, 1000);
                            }
                        });
                    }
                });
            }
        });
    }
}

