package android.com.galatube;

import android.app.ProgressDialog;
import android.com.galatube.Connectivity.GEAlertDialog;
import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.GEUserModal.GEUserManager;
import android.com.galatube.Connectivity.GENetworkState;
import android.com.galatube.GEWebView.WebViewActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.android.gms.common.api.Status;

public class SettingActivity extends AppCompatActivity implements View.OnClickListener,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{
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
    private ProgressDialog mProgressDialog;
    private LinearLayout mGoogleSignInId;
    private TextView mGoogleSignInIdTv;
    private View mSwitchBtn;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setElevation(0);
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        mSignView=(View)findViewById(R.id.signout);
        mGoogleSignIn=(LinearLayout)findViewById(R.id.google_signin);
        mGoogleSignInId=(LinearLayout)findViewById(R.id.google_signinId);
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
        mGoogleSignInIdTv=(TextView)findViewById(R.id.GoogleSignInId_tv);
        mSwitchBtn=(Switch)findViewById(R.id.simpleSwitch);
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
        mGoogleSignInId.setOnClickListener(this);

        GEUserManager lGEUsermanager=GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0)
        {
            updateUi(true);
        }else if(lGEUsermanager.getmUserInfo().getUserEmail().length() == 0){

        }
            // GoogleApiClient

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("662481036351-ugbo6k3vu4evfb2runa0f8fav7nt66kk.apps.googleusercontent.com")
                .requestScopes(
                        new Scope("https://www.googleapis.com/auth/youtube"),
                        new Scope("https://www.googleapis.com/auth/youtube.upload"))
                .requestEmail()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API,gso)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener(this)
                .build();
        Auth.GoogleSignInApi.silentSignIn(googleApiClient);
    }

    @Override
    protected void onStart() {
        super.onStart();
        applyTheme();
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

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.google_signin:

                if(GENetworkState.isNetworkStatusAvialable (getApplicationContext())) {
                    OptionalPendingResult<GoogleSignInResult> opr = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
                    // If the user has not previously signed in on this device or the sign-in has expired,
                    // this asynchronous branch will attempt to sign in the user silently.  Cross-device
                    // single sign-on will occur in this branch.
                    showProgressDialog();
                    opr.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                        @Override
                        public void onResult(GoogleSignInResult googleSignInResult) {
                            hideProgressDialog();
                            handleResult(googleSignInResult);
                        }
                    });

                    signIn();
                } else {

                    GEAlertDialog.checkForAlertbox(this);
                }

                break;
            case R.id.google_signout:
                if(GENetworkState.isNetworkStatusAvialable (getApplicationContext())) {
                    signOut();
                } else {
                    GEAlertDialog.checkForAlertbox(this);

                }

                break;
            case R.id.videoEvent:
                Log.i("videoevent","videoevent");
                break;
            case R.id.themeEvent:
                Log.i("themeEvent","themeEvent");
                Intent intent=new Intent(SettingActivity.this,GEThemeActvity.class);
                startActivity(intent);
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

                Intent intent1=new Intent(SettingActivity.this, WebViewActivity.class);
                intent1.putExtra("Facebook",1);
                startActivity(intent1);
                Log.i("facebookEvent","facebookEvent");
                break;
            case R.id.twitterEvent:

                    Intent intent2 = new Intent(SettingActivity.this, WebViewActivity.class);
                    intent2.putExtra("Twitter", 2);
                    startActivity(intent2);
                    Log.i("twitterEvent", "twitterEvent");

                break;
            case R.id.instagramEvent:

                    Intent intent3 = new Intent(SettingActivity.this, WebViewActivity.class);
                    intent3.putExtra("Instagram", 3);
                    startActivity(intent3);
                    Log.i("instagramEvent", "instagramEvent");

                break;
            case R.id.googleEvent:

                    Intent intent4 = new Intent(SettingActivity.this, WebViewActivity.class);
                    intent4.putExtra("GooglePlus", 4);
                    startActivity(intent4);
                    Log.i("googleEvent", "googleEvent");

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
        GEUserManager lManager = GEUserManager.getInstance(getApplicationContext());
        lManager.resetData();
        Auth.GoogleSignInApi.revokeAccess(googleApiClient);
      Auth.GoogleSignInApi.signOut(googleApiClient).setResultCallback(new ResultCallback<Status>() {
          @Override
          public void onResult(@NonNull Status status) {
//
              updateUi(false);
          }
      });
    }

    public void handleResult(GoogleSignInResult result){
           if (result.isSuccess()){
               GoogleSignInAccount account=result.getSignInAccount();
               String name=account.getDisplayName();
               String tokenId=account.getIdToken();
               String email=account.getEmail();
               String image =account.getPhotoUrl().toString();
               GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
               lGEUserManager.setUserName(name);
               lGEUserManager.setUserId(tokenId);
               lGEUserManager.setUserEmail(email);
               lGEUserManager.setUserImageUrl(image);
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
          mGoogleSignInId.setVisibility(View.VISIBLE);
          mGoogleSignIn.setVisibility(View.GONE);
          GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
          mGoogleSignInIdTv.setText(lGEUserManager.getmUserInfo().getUserEmail());
      }
        else {
          mGoogleSignOut.setVisibility(View.GONE);
          mSignView.setVisibility(View.GONE);
          mGoogleSignInId.setVisibility(View.GONE);
          mGoogleSignIn.setVisibility(View.VISIBLE);
      }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        hideProgressDialog();
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }
    private void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(this);
            mProgressDialog.setMessage("Please Wait...");
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    private void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.hide();
        }
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
