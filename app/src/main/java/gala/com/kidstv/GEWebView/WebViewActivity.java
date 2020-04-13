package gala.com.kidstv.GEWebView;

import android.app.ProgressDialog;
import gala.com.kidstv.Connectivity.GENetworkState;
import gala.com.kidstv.GETheme.GEThemeManager;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;

import gala.com.kidstv.R;
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
        setContentView(gala.com.kidstv.R.layout.activity_web_view);
        Toolbar lToolBar = (Toolbar)findViewById(gala.com.kidstv.R.id.toolbar);
        setSupportActionBar(lToolBar);
        getSupportActionBar().setTitle("");
        TextView mTitleBar = (TextView)lToolBar.findViewById(R.id.toolbar_title);
        getSupportActionBar().setElevation(0);
        Intent intent = getIntent();
        String lTitle = intent.getExtras().getString("Title");
        mTitleBar.setText(lTitle);

        applyTheme();
        mWebView=(WebView)findViewById(gala.com.kidstv.R.id.webview);
        mProgressBar = (ProgressBar)findViewById(gala.com.kidstv.R.id.progressBar);
        mWebView.setWebViewClient(new CustomWebViewClient());
        mGEWebsetting=mWebView.getSettings();
        mGEWebsetting.setJavaScriptEnabled(true);
        mGEWebsetting.setDisplayZoomControls(true);
        if(!GENetworkState.isNetworkStatusAvialable(this)){
            mNetwrk=(RelativeLayout)findViewById(gala.com.kidstv.R.id.netwrk);
            mNetwrk.setVisibility(View.VISIBLE);
            mReloadPage=(ImageButton)findViewById(gala.com.kidstv.R.id.reload_page);
            mReloadPage.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (GENetworkState.isNetworkStatusAvialable(getBaseContext())) {
                        mNetwrk.setVisibility(v.GONE);
                        Intent intent = getIntent();
                        String lUrl = intent.getExtras().getString("URL");
                        mWebView.loadUrl(lUrl);
                    }
                }
            });
        }else {
            String lUrl = intent.getExtras().getString("URL");
            mWebView.loadUrl(lUrl);
        }

    }

    private void applyTheme() {
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        ActionBar lActionBar = getSupportActionBar();
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        int lTextColor = GEThemeManager.getInstance(this).getSelectedNavTextColor();
        ColorDrawable lColorDrawable = new ColorDrawable(lColor);
        lActionBar.setBackgroundDrawable(lColorDrawable);
        Toolbar lToolBar = (Toolbar)findViewById(gala.com.kidstv.R.id.toolbar);
        lToolBar.setTitleTextColor(lTextColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
    }

    private class CustomWebViewClient extends WebViewClient {
        @Override
        public boolean shouldOverrideUrlLoading(WebView view, String url) {

            if(url.startsWith("mailto")){
                handleMailToLink(url);
                return true;
            }

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



        protected void handleMailToLink(String url){

            // Empty the text view
            // Extract the email address from mailto url
            try {
                url = URLDecoder.decode(url, "UTF-8");
            } catch (UnsupportedEncodingException e) {
                e.printStackTrace();
            }
            String to = url.split("[:?]")[1];
            String subject = "";
            // Extract the subject
            if(url.contains("subject=")){
                subject = url.split("subject=")[1];
            }

            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", to, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, subject);
            startActivity(Intent.createChooser(emailIntent, null));

        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
