package gala.com.urtube;

import android.app.ProgressDialog;
import gala.com.urtube.Connectivity.GEAlertDialog;
import gala.com.urtube.GETheme.GEThemeManager;
import gala.com.urtube.GEUserModal.GEUserManager;
import gala.com.urtube.Connectivity.GENetworkState;
import gala.com.urtube.GEWebView.WebViewActivity;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityOptionsCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

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
    private LinearLayout mAppTour;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;
    private TextView mGoogleSignInTv;
    private ProgressDialog mProgressDialog;
    private LinearLayout mGoogleSignInId;
    private TextView mGoogleSignInIdTv;
    private View mSwitchBtn;
    private ProgressBar mProgressBar;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        Toolbar lToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(lToolBar);
        getSupportActionBar().setTitle("Setting");
        getSupportActionBar().setElevation(0);
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));

        mProgressBar = (ProgressBar)findViewById(R.id.progressBar);
        mProgressBar.setVisibility(View.INVISIBLE);
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
        mAppTour=(LinearLayout)findViewById(R.id.apptour);

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
        mGoogleSignInId.setOnClickListener(this);
        mAppTour.setOnClickListener(this);

        GEUserManager lGEUsermanager=GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0)
        {
            updateUi(true);
        }else if(lGEUsermanager.getmUserInfo().getUserEmail().length() == 0){

        }
            // GoogleApiClient

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("403770149720-l50se8m410h1qi57d54q5stmiq95vtt8.apps.googleusercontent.com")
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
        int lTextColor = GEThemeManager.getInstance(this).getSelectedNavTextColor();
        ColorDrawable lColorDrawable = new ColorDrawable(lColor);
        lActionBar.setBackgroundDrawable(lColorDrawable);
        Toolbar lToolBar = (Toolbar)findViewById(R.id.toolbar);
        lToolBar.setTitleTextColor(lTextColor);

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
                            mProgressBar.setVisibility(View.INVISIBLE);
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
                Intent intent1=new Intent(SettingActivity.this, WebViewActivity.class);
                intent1.putExtra("URL","file:///android_asset/www/disclaimer.html");
                intent1.putExtra("Title","Disclaimer");
                startActivity(intent1);
                Log.i("disclaimerEvent","disclaimerEvent");
                break;
            case R.id.aboutEvent:
                intent1=new Intent(SettingActivity.this, WebViewActivity.class);
                intent1.putExtra("URL","file:///android_asset/www/aboutapp.html");
                intent1.putExtra("Title","About App");
                startActivity(intent1);
                Log.i("aboutEvent","aboutEvent");
                break;

            case R.id.apptour:
                Bundle bundle = ActivityOptionsCompat.makeCustomAnimation(getBaseContext(), android.R.anim.fade_in, android.R.anim.fade_out).toBundle();
                Intent i = new Intent(SettingActivity.this, GEIntroActivity.class);
                i.putExtra("Dismiss",true);
                startActivity(i, bundle);
                Log.i("aboutEvent","aboutEvent");
                break;

            case R.id.shareEvent:

                mProgressBar.setVisibility(View.VISIBLE);

                DynamicLink dynamicLink = FirebaseDynamicLinks.getInstance().createDynamicLink()
                        .setLink(Uri.parse("https://urtube.com/"))
                        .setDynamicLinkDomain("vnpy3.app.goo.gl")
                        // Open links with this app on Android
                        .setAndroidParameters(new DynamicLink.AndroidParameters.Builder().build())
                        // Open links with com.example.ios on iOS
                        .buildDynamicLink();

                Uri dynamicLinkUri = dynamicLink.getUri();


                Task<ShortDynamicLink> shortLinkTask = FirebaseDynamicLinks.getInstance().createDynamicLink()
//                        .setLink(Uri.parse("https://example.com/"))
                        .setLongLink(dynamicLinkUri)
                        .buildShortDynamicLink()
                        .addOnCompleteListener(this, new OnCompleteListener<ShortDynamicLink>() {
                            @Override
                            public void onComplete(@NonNull Task<ShortDynamicLink> task) {
                                mProgressBar.setVisibility(View.INVISIBLE);
                                if (task.isSuccessful()) {
                                    // Short link created
                                    Uri shortLink = task.getResult().getShortLink();
                                    Uri flowchartLink = task.getResult().getPreviewLink();
                                    Intent sharingIntent = new Intent(Intent.ACTION_SEND);
                                    sharingIntent.setType("text/plain");
                                    sharingIntent.putExtra(Intent.EXTRA_SUBJECT, "Enjoy UR Tube Android App");
                                    sharingIntent.putExtra(Intent.EXTRA_TEXT, "It's a great app for catching up on Comedy, Funny Prank, Stand Up Comedy, Dramas, Short\n" +
                                            " Films & Epic Stories you might not have heard of otherwise.\n\n Google Play Store link \uD83D\uDC47\uD83C\uDFFD \n " + shortLink.toString());
                                    startActivity(Intent.createChooser(sharingIntent, "Share App"));

                                } else {
                                    // Error
                                    Log.e("TAG", "Short Dynamic link error", task.getException());

                                    // ...
                                }
                            }
                        });

                Log.i("shareEvent","shareEvent");
                break;
            case R.id.supportEvent:
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "urtubefeedback@gmail.com", null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "Feedback to UR Tube Support Team");
                startActivity(Intent.createChooser(emailIntent, null));

                Log.i("supportEvent","supportEvent");
                break;
            case R.id.rateEvent:
                Uri uri = Uri.parse("market://details?id=" + "com.google.android.youtube");
                Intent goToMarket = new Intent(Intent.ACTION_VIEW, uri);
                // To count with Play market backstack, After pressing back button,
                // to taken back to our application, we need to add following flags to intent.
                goToMarket.addFlags(Intent.FLAG_ACTIVITY_NO_HISTORY |
                        Intent.FLAG_ACTIVITY_NEW_DOCUMENT |
                        Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
                try {
                    startActivity(goToMarket);
                } catch (ActivityNotFoundException e) {
                    startActivity(new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://play.google.com/store/apps/details?id=" + getPackageName())));
                }
                Log.i("rateEvent","rateEvent");
                break;
            case R.id.facebookEvent:
                intent1=new Intent(SettingActivity.this, WebViewActivity.class);
                intent1.putExtra("URL","https://www.facebook.com/URTubeOfficial/");
                intent1.putExtra("Title","Facebook Page");
                startActivity(intent1);
                Log.i("facebookEvent","facebookEvent");
                break;
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void signIn(){
        mProgressBar.setVisibility(View.VISIBLE);
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }


    public void signOut(){
        GEUserManager lManager = GEUserManager.getInstance(getApplicationContext());
        lManager.resetData();
//        Auth.GoogleSignInApi.revokeAccess(googleApiClient);
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
               String image = "";
               if (account.getPhotoUrl() != null)
                   image = account.getPhotoUrl().toString();

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

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
