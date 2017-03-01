package android.com.galatube;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener{
    private TextView mClose_Setting;
    private LinearLayout mGoogleSignIn;
    private LinearLayout mGoogleSignOut;
    private View mSignView;
    private LinearLayout mVideolEvent;
    private LinearLayout mThemelEvent;
    private LinearLayout mDieclaimerlEvent;
    private LinearLayout mAboutlEvent;
    private LinearLayout mSharelEvent;
    private LinearLayout mSupportlEvent;
    private LinearLayout mRatelEvent;
    private LinearLayout mFacebooklEvent;
    private LinearLayout mTwitterlEvent;
    private LinearLayout mInstagram;
    private LinearLayout mGooglelEvent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setElevation(0);
        mSignView=(View)findViewById(R.id.signout);
        mGoogleSignIn=(LinearLayout)findViewById(R.id.google_signin);
        mGoogleSignOut=(LinearLayout)findViewById(R.id.google_signout);
        mVideolEvent=(LinearLayout)findViewById(R.id.videoEvent);
        mThemelEvent=(LinearLayout)findViewById(R.id.themeEvent);
        mDieclaimerlEvent=(LinearLayout)findViewById(R.id.disclaimerEvent);
        mAboutlEvent=(LinearLayout)findViewById(R.id.aboutEvent);
        mSharelEvent=(LinearLayout)findViewById(R.id.shareEvent);
        mSupportlEvent=(LinearLayout)findViewById(R.id.supportEvent);
        mRatelEvent=(LinearLayout)findViewById(R.id.rateEvent);
        mFacebooklEvent=(LinearLayout)findViewById(R.id.facebookEvent);
        mTwitterlEvent=(LinearLayout)findViewById(R.id.twitterEvent);
        mInstagram=(LinearLayout)findViewById(R.id.instagramEvent);
        mGooglelEvent=(LinearLayout)findViewById(R.id.googleEvent);
        mGoogleSignIn.setOnClickListener(this);
        mGoogleSignOut.setOnClickListener(this);
        mVideolEvent.setOnClickListener(this);
        mThemelEvent.setOnClickListener(this);
        mDieclaimerlEvent.setOnClickListener(this);
        mAboutlEvent.setOnClickListener(this);
        mSharelEvent.setOnClickListener(this);
        mSupportlEvent.setOnClickListener(this);
        mRatelEvent.setOnClickListener(this);
        mFacebooklEvent.setOnClickListener(this);
        mTwitterlEvent.setOnClickListener(this);
        mInstagram.setOnClickListener(this);
        mGooglelEvent.setOnClickListener(this);


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_signin:
                mGoogleSignOut.setVisibility(v.VISIBLE);
                mSignView.setVisibility(v.VISIBLE);
                break;
            case R.id.google_signout:
                mGoogleSignOut.setVisibility(v.GONE);
                mSignView.setVisibility(v.GONE);
                break;
            case R.id.videoEvent:
                Log.i("videoevent","videoevent");
                break;
            case R.id.themeEvent:
                Log.i("themeEvent","themeEvent");
                break;
            case R.id.disclaimerEvent:
                Log.i("disclaimerEvent","disclaimerEvent");
                break;
            case R.id.aboutEvent:
                Log.i("aboutEvent","aboutEvent");
                break;
            case R.id.shareEvent:
                Log.i("shareEvent","shareEvent");
                break;
            case R.id.supportEvent:
                Log.i("supportEvent","supportEvent");
                break;
            case R.id.rateEvent:
                Log.i("rateEvent","rateEvent");
                break;
            case R.id.facebookEvent:
                Log.i("facebookEvent","facebookEvent");
                break;
            case R.id.twitterEvent:
                Log.i("twitterEvent","twitterEvent");
                break;
            case R.id.instagramEvent:
                Log.i("instagramEvent","instagramEvent");
                break;
            case R.id.googleEvent:
                Log.i("googleEvent","googleEvent");
                break;
        }
    }
}
