package android.com.galatube;

import android.content.Intent;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;

import static android.view.View.VISIBLE;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{
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
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;
    private TextView mGoogleSignInTv;


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
        mGoogleSignInTv=(TextView)findViewById(R.id.GoogleSignIn_tv);
        mGoogleSignOut.setVisibility(View.GONE);
        mSignView.setVisibility(View.GONE);
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

        // GoogleApiClient
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();


    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_signin:
                signIn();
                break;
            case R.id.google_signout:
                signOut();
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

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    public void signIn(){
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }


    public void signOut(){
      Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
          @Override
          public void onResult(@NonNull Status status) {
              updateUi(false);
          }
      });
    }

    public void handleResult(GoogleSignInResult result){
           if (result.isSuccess()){
               GoogleSignInAccount account=result.getSignInAccount();
               String name=account.getDisplayName();
               String email=account.getEmail();
               String image =account.getPhotoUrl().toString();
               mGoogleSignInTv.setText(email);
               updateUi(true);
           }
        else
           {
               updateUi(false);
           }
    }

    public void updateUi(boolean isLogin){
      if (isLogin){
          mGoogleSignOut.setVisibility(View.VISIBLE);
          mSignView.setVisibility(View.VISIBLE);
      }
        else {
          mGoogleSignOut.setVisibility(View.GONE);
          mSignView.setVisibility(View.GONE);
          mGoogleSignInTv.setText("GoogleSignIn");
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
         if(requestCode==REQ_CODE){
             GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
             handleResult(result);
         }
    }
}
