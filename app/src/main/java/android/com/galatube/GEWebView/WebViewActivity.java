package android.com.galatube.GEWebView;

import android.app.ProgressDialog;
import android.com.galatube.Connectivity.GENetworkState;
import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.R;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class WebViewActivity extends AppCompatActivity {

    private WebView mWebView;
    private WebSettings mGEWebsetting;
    private ProgressDialog Webdialog;
    private RelativeLayout mRelativeWebView;
    private RelativeLayout mNetwrk;
    private ImageButton mReloadPage;
    private ProgressBar mProgressBar;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_web_view);
        getSupportActionBar().setElevation(0);
        applyTheme();
        mWebView=(WebView)findViewById(R.id.webview);
        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mWebView.setWebViewClient(new CustomWebViewClient());
        mGEWebsetting=mWebView.getSettings();
        mGEWebsetting.setJavaScriptEnabled(true);
        mGEWebsetting.setDisplayZoomControls(true);
        if(!GENetworkState.isNetworkStatusAvialable(this)){
            mNetwrk=(RelativeLayout)findViewById(R.id.netwrk);
            mNetwrk.setVisibility(View.VISIBLE);
            mReloadPage=(ImageButton)findViewById(R.id.reload_page);
            mReloadPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GENetworkState.isNetworkStatusAvialable(getBaseContext())) {
                        mNetwrk.setVisibility(v.GONE);
                        Intent intent = getIntent();
                        if (intent.getExtras().getInt("Facebook") == 1) {
                            mWebView.loadUrl("https://www.facebook.com/GalaEye/");

                        } else if (intent.getExtras().getInt("Twitter") == 2) {
                            mWebView.loadUrl("https://twitter.com/galaeye");

                        } else if (intent.getExtras().getInt("Instagram") == 3) {
                            mWebView.loadUrl("https://www.instagram.com/galaeye/");

                        } else if (intent.getExtras().getInt("GooglePlus") == 4) {
                            mWebView.loadUrl("https://plus.google.com/113955638351151088577");
                        }
                    }
                }
            });
        }else {

                Intent intent = getIntent();
            if (intent.getExtras().getInt("Facebook") == 1) {
                mWebView.loadUrl("https://www.facebook.com/GalaEye/");

            } else if (intent.getExtras().getInt("Twitter") == 2) {
                mWebView.loadUrl("https://twitter.com/galaeye");

            } else if (intent.getExtras().getInt("Instagram") == 3) {
                mWebView.loadUrl("https://www.instagram.com/galaeye/");

            } else if (intent.getExtras().getInt("GooglePlus") == 4) {
                mWebView.loadUrl("https://plus.google.com/113955638351151088577");
            }
        }

    }

    private void applyTheme() {
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        ActionBar lActionBar = getSupportActionBar();
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        ColorDrawable lColorDrawable = new ColorDrawable(lColor);
        lActionBar.setBackgroundDrawable(lColorDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            mProgressBar.setVisibility(View.VISIBLE);
            mProgressBar.getIndeterminateDrawable().setColorFilter(Color.parseColor("#C0D000"), android.graphics.PorterDuff.Mode.SRC_ATOP);
            view.loadUrl(url);
            return true;
        }
        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            mProgressBar.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
