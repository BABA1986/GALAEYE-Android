package gala.com.kidstv.kidsLauncher;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Typeface;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityOptionsCompat;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.BounceInterpolator;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.Timer;
import java.util.TimerTask;

import gala.com.kidstv.Refactored.GEAppMainActiviry;
import gala.com.kidstv.GEIntroActivity;
import gala.com.kidstv.GETheme.GEThemeManager;
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

        applyTheme();
        startanimation();
//
//        Button saveButton = (Button) findViewById(R.id.button000011);
//        saveButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                // do the "on click" action here
//                RelativeLayout lTVTextBaseLayout = (RelativeLayout) findViewById(R.id.textlayoutbase);
//                lTVTextBaseLayout.setVisibility(View.INVISIBLE);
//
//                RelativeLayout lSapBaseLayout = (RelativeLayout) findViewById(R.id.saperatorview);
//                lSapBaseLayout.setVisibility(View.INVISIBLE);
//
//                ImageView lImageView = (ImageView) findViewById(R.id.videoicon);
//                lImageView.setVisibility(View.VISIBLE);
//
//                startanimation();
//            }
//        });
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
                kidsCharacterAnimation();
            }
        });
    }

    public void kidsCharacterAnimation() {
        TranslateAnimation lTvTextTranslateAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
        lTvTextTranslateAnim.setFillAfter(true);
        lTvTextTranslateAnim.setDuration(800);
        GEBounceInterPolator lInterpolator = new GEBounceInterPolator(0.3, 7);

        lTvTextTranslateAnim.setInterpolator(lInterpolator);
        RelativeLayout lTVTextBaseLayout = (RelativeLayout) findViewById(R.id.textlayoutbase);
        lTVTextBaseLayout.setVisibility(View.VISIBLE);
        lTVTextBaseLayout.setAnimation(lTvTextTranslateAnim);
        lTVTextBaseLayout.startAnimation(lTvTextTranslateAnim);

        lTvTextTranslateAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

                    TranslateAnimation lSapTranslateAnim = new TranslateAnimation(TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_PARENT, 0.0f, TranslateAnimation.RELATIVE_TO_SELF, 1.0f, TranslateAnimation.RELATIVE_TO_SELF, 0.0f);
                    lSapTranslateAnim.setFillAfter(true);
                    lSapTranslateAnim.setDuration(800);
                    lSapTranslateAnim.setStartOffset(100);

                    GEBounceInterPolator lInterpolator = new GEBounceInterPolator(0.3, 7);

                    lSapTranslateAnim.setInterpolator(lInterpolator);
                    RelativeLayout lSapBaseLayout = (RelativeLayout) findViewById(R.id.saperatorview);
                    lSapBaseLayout.setVisibility(View.VISIBLE);
                    lSapBaseLayout.setAnimation(lSapTranslateAnim);
                    lSapBaseLayout.startAnimation(lSapTranslateAnim);
                    lSapTranslateAnim.setAnimationListener(new Animation.AnimationListener() {
                        @Override
                        public void onAnimationStart(Animation animation) {
                            TranslateAnimation lVidImageTranslateAnim1 = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, -100f);
                            lVidImageTranslateAnim1.setDuration(200);
                            lVidImageTranslateAnim1.setFillAfter(true);
                            TranslateAnimation lVidImageTranslateAnim2 = new TranslateAnimation(TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, 0.0f, TranslateAnimation.ABSOLUTE, 100f);
                            lVidImageTranslateAnim2.setDuration(800);
                            lVidImageTranslateAnim2.setFillAfter(true);
                            AnimationSet lAnimationSet = new AnimationSet(true);
                            lAnimationSet.setStartOffset(200);
                            lAnimationSet.addAnimation(lVidImageTranslateAnim1);
                            lAnimationSet.addAnimation(lVidImageTranslateAnim2);

//                            GEBounceInterPolator lInterpolator = new GEBounceInterPolator(0.1, 10);
//                            lAnimationSet.setInterpolator(lInterpolator);
                            ImageView lImageView = (ImageView) findViewById(R.id.videoicon);
                            lImageView.setVisibility(View.VISIBLE);
                            lImageView.setAnimation(lAnimationSet);
                            lImageView.startAnimation(lAnimationSet);
                            lAnimationSet.setAnimationListener(new Animation.AnimationListener() {
                                @Override
                                public void onAnimationStart(Animation animation) {

                                }

                                @Override
                                public void onAnimationEnd(Animation animation) {
                                    TextView lTVText = (TextView) findViewById(R.id.tvtext);
                                    ObjectAnimator lTVAnimation = ObjectAnimator.ofFloat(lTVText, "rotationX", 180.0f, 0.0f);
                                    lTVAnimation.setDuration(2000);
                                    lTVAnimation.setRepeatCount(0);
                                    lTVAnimation.setInterpolator(new BounceInterpolator());
                                    lTVAnimation.start();

                                    TextView lSText = (TextView) findViewById(R.id.stext);
                                    ObjectAnimator lSTextAnimation = ObjectAnimator.ofFloat(lSText, "rotationY", 180.0f, 0.0f);
                                    lSTextAnimation.setDuration(1200);
                                    lSTextAnimation.setRepeatCount(0);
                                    lSTextAnimation.setInterpolator(new BounceInterpolator());
                                    lSTextAnimation.start();

                                    lSTextAnimation.addListener(new Animator.AnimatorListener() {
                                        @Override
                                        public void onAnimationStart(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationEnd(Animator animation) {
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
                                                        Intent i = new Intent(GEKidsLauncherActivity.this, GEIntroActivity.class);
                                                        i.putExtra("Dismiss", false);
                                                        startActivity(i, bundle);
                                                        finish();
                                                    }
                                                    else {
//                                                        Intent i = new Intent(GEKidsLauncherActivity.this, GEMainMenuActivity.class);
                                                        //NewLayout-DK Related Code
                                                        Intent i = new Intent(GEKidsLauncherActivity.this, GEAppMainActiviry.class);
                                                        startActivity(i);
                                                        finish();
                                                    }
                                                    overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
                                                }
                                            }, 1000);
                                        }

                                        @Override
                                        public void onAnimationCancel(Animator animation) {

                                        }

                                        @Override
                                        public void onAnimationRepeat(Animator animation) {

                                        }
                                    });
                                }

                                @Override
                                public void onAnimationRepeat(Animation animation) {

                                }
                            });
                        }

                        @Override
                        public void onAnimationEnd(Animation animation) {

                        }

                        @Override
                        public void onAnimationRepeat(Animation animation) {

                        }
                    });
            }

            @Override
            public void onAnimationEnd(Animation animation) {

            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

    }
}
