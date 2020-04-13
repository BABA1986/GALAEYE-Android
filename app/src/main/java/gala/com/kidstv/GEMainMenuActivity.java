package gala.com.kidstv;

import android.accounts.Account;

import gala.com.kidstv.GEPlayer.GEInterstitialAdMgr;
import gala.com.kidstv.GEPlayer.GEPlayerActivity;
import gala.com.kidstv.GETheme.GEThemeManager;
import gala.com.kidstv.GEUserModal.GEUserManager;
import gala.com.kidstv.Connectivity.GENetworkState;
import gala.com.kidstv.GEYoutubeEvents.GEEventTypes;
import gala.com.kidstv.model.GEMenu.GEMenu;
import gala.com.kidstv.model.GEMenu.GEMenuAdapter;
import gala.com.kidstv.model.GEMenu.GESharedMenu;
import gala.com.kidstv.model.GEMenu.GESubMenu;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.google.android.material.navigation.NavigationView;
import com.google.android.material.tabs.TabLayout;
import androidx.core.view.GravityCompat;
import androidx.viewpager.widget.ViewPager;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.graphics.drawable.DrawerArrowDrawable;
import androidx.appcompat.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.github.javiersantos.appupdater.AppUpdater;
import com.github.javiersantos.appupdater.enums.Display;
import com.github.javiersantos.appupdater.enums.UpdateFrom;
import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.OptionalPendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Scope;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

/**
 * Created by deepak on 30/11/16.
 */

public class GEMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener, GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks {

    private LinearLayout mSettingLayout;
    private LinearLayout mGoogleNavigationSignIn;
    private TextView mWelcom_SignIn;
    private TextView mSignIn_Navigation;
    private ImageView mUserIv;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE = 9001;
    private LinearLayout mGooGleHeader;
    DrawerLayout mDrawer;
    private Toolbar mtoolbar;
    private TextView mToolbarTitle;
    private LinearLayout mTabToolbar;
    private Menu mActionBarMenu;
    private int mDrawerSelectedMenuIndex;
    ArrayList<GEMenu> mMenuItems;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        // App Updater dialog

        AppUpdater appUpdater = new AppUpdater(this);
        appUpdater.setDisplay(Display.DIALOG)
                .setUpdateFrom(UpdateFrom.GOOGLE_PLAY)
                .showEvery(3)
                .setTitleOnUpdateAvailable("Update available")
                .setContentOnUpdateAvailable("New version of KidsTV is available on Play Store. Please update KidsTV application to get new fetures and improvements.")
                .setButtonUpdate("Update now?")
                .setButtonDismiss("Maybe later")
                .setButtonDoNotShowAgain(null)
                .setCancelable(false)
                .start();


        mMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();


        MobileAds.initialize(this,"ca-app-pub-5685624800532639/7200115513");
        GEInterstitialAdMgr.initWithContext(this);

        final AdView lAdView = (AdView) findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().tagForChildDirectedTreatment(true).build();
        lAdView.loadAd(adRequest);
        lAdView.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                lAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
                lAdView.setVisibility(View.GONE);
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when an ad opens an overlay that
                // covers the screen.
                lAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
                lAdView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the user is about to return
                // to the app after tapping on an ad.
//                lAdView.setVisibility(View.GONE);
            }
        });


        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("");
        mToolbarTitle = (TextView)mtoolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("Theme");
        mGoogleNavigationSignIn = (LinearLayout) findViewById(R.id.Google_Navigation_Header);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabToolbar = (LinearLayout) findViewById(R.id.tab_toolbar);
        mWelcom_SignIn = (TextView) findViewById(R.id.welcome_tv);
        mSignIn_Navigation = (TextView) findViewById(R.id.signIn_tv);
        mUserIv = (ImageView) findViewById(R.id.imageView);
        mUserIv.setOnClickListener(this);
        mSettingLayout = (LinearLayout) findViewById(R.id.setting_base_adapter);
        mDrawer.setDrawerListener(new DrawerLayout.DrawerListener() {
                                     @Override
                                     public void onDrawerSlide(View drawerView, float slideOffset) {
                                     }

                                     @Override
                                     public void onDrawerOpened(View drawerView) {
                                     }

                                     @Override
                                     public void onDrawerClosed(View drawerView) {
                                         GEInterstitialAdMgr.showInterstitialAd();
                                     }

                                     @Override
                                     public void onDrawerStateChanged(int newState) {
                                     }
                                 });

        mSettingLayout.setOnClickListener(this);
        mGoogleNavigationSignIn.setOnClickListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        ImageLoader imageLoader = ImageLoader.getInstance();
        DisplayImageOptions defaultOptions = new DisplayImageOptions.Builder()
                .bitmapConfig(Bitmap.Config.RGB_565)
                .cacheOnDisk(true)
                .cacheInMemory(false)
                .build();

        ImageLoaderConfiguration config = new ImageLoaderConfiguration
                .Builder(getBaseContext())
                .defaultDisplayImageOptions(defaultOptions)
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileNameGenerator(new Md5FileNameGenerator())
                .build();
        imageLoader.init(config);

        ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
        GEMenu lMenuInfo = lMenuItems.get(0);
        lMenuInfo.setSelected(true);
        mToolbarTitle.setText(lMenuInfo.getmMenuName());
        final ListView lListView = (ListView) findViewById(R.id.left_drawer_list);
        lListView.setAdapter(new GEMenuAdapter(this, lMenuItems));
        lListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                resetSelection();
                ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
                GEMenu lMenuInfo = lMenuItems.get(position);
                lMenuInfo.setSelected(true);
                mDrawerSelectedMenuIndex = position;
                mDrawer.closeDrawers();
                lListView.invalidateViews();

                mMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
                initialisePagesForMenu(mMenuItems.get(mDrawerSelectedMenuIndex), "video");
            }
        });

        GEUserManager lGEUsermanager = GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0) {
            updateUi(true);
        }

        mDrawerSelectedMenuIndex = 0;

        initialisePagesForMenu(lMenuItems.get(mDrawerSelectedMenuIndex), "video");
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestServerAuthCode("348352912171-h20m86clr8g8jurkcsekj3n4tg480r5t.apps.googleusercontent.com")
                .requestScopes(
                        new Scope("https://www.googleapis.com/auth/youtube"),
                        new Scope("https://www.googleapis.com/auth/youtube.upload"),
                new Scope("https://www.googleapis.com/auth/youtube.force-ssl"))
                .requestEmail()
                .requestIdToken("348352912171-h20m86clr8g8jurkcsekj3n4tg480r5t.apps.googleusercontent.com")
                .requestId()
                .build();

        googleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener(this)
                .build();

        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
               openPlayerThroughNotificationIntent(getIntent());
            }
        }, 1000);
    }

    public String md5(String s) {
        try {
            // Create MD5 Hash
            MessageDigest digest = java.security.MessageDigest.getInstance("MD5");
            digest.update(s.getBytes());
            byte messageDigest[] = digest.digest();

            // Create Hex String
            StringBuffer hexString = new StringBuffer();
            for (int i=0; i<messageDigest.length; i++)
                hexString.append(Integer.toHexString(0xFF & messageDigest[i]));
            return hexString.toString();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    @Override
    protected void onNewIntent(Intent intent){
        super.onNewIntent(intent);

        openPlayerThroughNotificationIntent(intent);
    }

    private void openPlayerThroughNotificationIntent(Intent intent){
        String lchannelId = intent.getStringExtra("channelid");
        String lVideolId = intent.getStringExtra("videoid");
        Boolean lIsChannelID = intent.getBooleanExtra("ischannelId", true);

        if (lchannelId == null)
            return;

        if (lVideolId == null)
            return;

        Intent lIntent = new Intent(this, GEPlayerActivity.class);
        lIntent.putExtra("channelid", lchannelId);
        lIntent.putExtra("eventtype", GEEventTypes.EFetchEventsArchivedVideos);
        lIntent.putExtra("ischannelId", lIsChannelID);
        lIntent.putExtra("videoid", lVideolId);
        lIntent.putExtra("selectedIndex", -1);
        startActivity(lIntent);
    }

    @Override
    protected void onStart() {
        super.onStart();

        OptionalPendingResult<GoogleSignInResult> lOpt = Auth.GoogleSignInApi.silentSignIn(googleApiClient);
        if (lOpt.isDone())
        {
            GoogleSignInResult lResult = lOpt.get();
            try {
                handleResult(lResult);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        else
        {
            lOpt.setResultCallback(new ResultCallback<GoogleSignInResult>() {
                @Override
                public void onResult(GoogleSignInResult googleSignInResult) {
                    try {
                        handleResult(googleSignInResult);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            });
        }

        applyTheme();
        GEUserManager lGEUsermanager = GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0) {
            updateUi(true);
        } else {
            updateUi(false);
        }

        applyFontOnTab();
    }

    @Override
    protected void onResume() {
        super.onResume();
        applyFontOnTab();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        GEInterstitialAdMgr.showInterstitialAd();
    }

    private void applyFontOnTab(){
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
                ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
                int tabsCount = vg.getChildCount();
                for (int j = 0; j < tabsCount; j++) {
                    ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
                    int tabChildsCount = vgTab.getChildCount();
                    for (int i = 0; i < tabChildsCount; i++) {
                        final View tabViewChild = vgTab.getChildAt(i);
                        if (tabViewChild instanceof TextView) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    ((TextView) tabViewChild).setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));
                                }
                            });
                        }
                    }
                }
            }
        }, 5);
    }

    private void applyTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("myTheme", MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition", 0));
        ActionBar lActionBar = getSupportActionBar();
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        int lTextColor = GEThemeManager.getInstance(this).getSelectedNavTextColor();
        mGoogleNavigationSignIn.setBackgroundColor(lColor);
        mTabToolbar.setBackgroundColor(lColor);
        mtoolbar.setBackgroundColor(lColor);
        ColorDrawable lColorDrawable = new ColorDrawable(lColor);

        mWelcom_SignIn.setTextColor(lTextColor);
        mSignIn_Navigation.setTextColor(lTextColor);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer , mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        DrawerArrowDrawable arrow = drawerToggle.getDrawerArrowDrawable();
        if(arrow != null) {
            final PorterDuffColorFilter colorFilter = new PorterDuffColorFilter(lTextColor, PorterDuff.Mode.SRC_ATOP);
            arrow.setColor(lColor);
            arrow.setColorFilter(colorFilter);
            mtoolbar.setNavigationIcon(arrow);
        }

        mToolbarTitle.setTextColor(lTextColor);
        mToolbarTitle.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        tabLayout.setSelectedTabIndicatorColor(lTextColor);
        tabLayout.setTabTextColors(lTextColor, lTextColor);
        tabLayout.setBackground(lColorDrawable);
//        tabLayout.setBackgroundColor(lColor);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }

        TextView lSettingTextView = (TextView) mSettingLayout.findViewById(R.id.setting_text);
        lSettingTextView.setTextColor(lTextColor);
        ImageView lSettingIconView = (ImageView) mSettingLayout.findViewById(R.id.setting_icon);
        Bitmap lBitmap = BitmapFactory.decodeResource(this.getResources(),
                R.drawable.settingicon);
        mSettingLayout.setBackgroundColor(lColor);

        GEThemeManager.getInstance(this).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        Bitmap resultBitmap = Bitmap.createBitmap(lBitmap, 0, 0,
                lBitmap.getWidth() - 1, lBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(lTextColor, PorterDuff.Mode.SRC_IN);
        p.setColorFilter(filter);
        lSettingIconView.setImageBitmap(resultBitmap);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
        ListView lListView = (ListView) findViewById(R.id.left_drawer_list);
        lListView.invalidateViews();
    }

    public void resetSelection()
    {
        ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
        for (int i = 0; i < lMenuItems.size(); ++i){
            GEMenu lMenuInfo = lMenuItems.get(i);
            lMenuInfo.setSelected(false);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.playlistvideofilter, menu);//Menu Resource, Menu
        mActionBarMenu = menu;
        showOptionMenuIfRequired();
        return true;
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        return false;
    }

    boolean doubleBackToExitPressedOnce = true;
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            return;
        }

        if (doubleBackToExitPressedOnce) {
            doubleBackToExitPressedOnce = false;
            Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();
            return;
        }

        super.onBackPressed();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
        GEMenu lMenu = lMenuItems.get(mDrawerSelectedMenuIndex);

        switch (item.getItemId()) {
            // action with ID action_refresh was selected
            case R.id.playlistsfilter:
                initialisePagesForMenu(lMenu, "playlists");
                break;
            // action with ID action_settings was selected
            case R.id.videofilter:
                initialisePagesForMenu(lMenu, "videos");
                break;
            default:
                break;
        }

        return true;
    }

    //In Case of playlist tipe menu only
    private void showOptionMenuIfRequired() {
        if (mActionBarMenu == null)
            return;

        boolean lShouldShow = false;
        ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
        GEMenu lMenu = lMenuItems.get(mDrawerSelectedMenuIndex);
        GESubMenu lSubMenu = lMenu.getmSubMenus().get(0);
        String lSubMenuType = lSubMenu.getmSubMenuType();

        if (lSubMenuType.equals("playlist") == true)
            lShouldShow = true;

        for (int i = 0; i < mActionBarMenu.size(); i++)
            mActionBarMenu.getItem(i).setVisible(lShouldShow);
    }

    public void initialisePagesForMenu(GEMenu menuInfo, String filter) {
        String lMenuName=menuInfo.getmMenuName();
        mToolbarTitle.setText(lMenuName);
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        GEPageAdapter lAdapter = (GEPageAdapter) viewPager.getAdapter();
        showOptionMenuIfRequired();
        if (lAdapter != null) {
            lAdapter.reloadWithSubMenu(menuInfo.getmSubMenus(), filter);
            viewPager.setCurrentItem(0);
            applyFontOnTab();
            return;
        }

        viewPager.setAdapter(new GEPageAdapter(getSupportFragmentManager(),
                GEMainMenuActivity.this, menuInfo.getmSubMenus(), filter));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);

        SharedPreferences sharedPreferences = getSharedPreferences("myTheme", MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition", 0));
        int lTextColor = GEThemeManager.getInstance(this).getSelectedNavTextColor();
        tabLayout.setSelectedTabIndicatorColor(lTextColor);
        tabLayout.setupWithViewPager(viewPager);
        viewPager.setCurrentItem(0);
        applyFontOnTab();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.setting_base_adapter:
                Intent intent = new Intent(GEMainMenuActivity.this, SettingActivity.class);
                startActivity(intent);
                break;
            case R.id.Google_Navigation_Header:
                if (GENetworkState.isNetworkStatusAvialable(getApplicationContext())) {
                    GEUserManager lGEUsermanager = GEUserManager.getInstance(getApplicationContext());
                    if (lGEUsermanager.getmUserInfo().getUserEmail().length() == 0) {
                        signIn();
                    }

                } else {
                    GEUserManager lGEUsermanager = GEUserManager.getInstance(getApplicationContext());
                    if (lGEUsermanager.getmUserInfo().getUserEmail().length() == 0) {

                        AlertDialog.Builder builder = new AlertDialog.Builder(this);
                        builder.setTitle("No Internet Connection");
                        builder.setMessage("Please turn on Internet connection to continue");
                        builder.setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                        AlertDialog alertDialog = builder.create();
                        alertDialog.show();
                    }
                    break;
                }
        }
    }


    public void signIn() {
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent, REQ_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQ_CODE) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            try {
                handleResult(result);
            } catch (IOException e) {
                Log.d("YC", "Could not initialize: " + e);
            }
        }
    }

    private void handleResult(GoogleSignInResult result) throws IOException {

        String lResult = result.toString();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference myRef = database.getReference("users");
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();

        if (result.isSuccess()) {
            GoogleSignInAccount account = result.getSignInAccount();

            Account lAccount = account.getAccount();

            String lAccessToken = (account != null) ? account.getServerAuthCode() : "";
            String lIDToken = (account != null) ? account.getIdToken() : "";
            String lID = (account != null) ? account.getId() : "";

            String name = account.getDisplayName();
            String email = account.getEmail();
            String image = account.getPhotoUrl().toString();
            GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
            lGEUserManager.setmGoogleAcct(lAccount);
            lGEUserManager.setUserName(name);
            lGEUserManager.setUserEmail(email);
            lGEUserManager.setUserImageUrl(image);
            lGEUserManager.setAuthToken(lAccessToken);
            lGEUserManager.setIdToken(lIDToken);
            lGEUserManager.setUserId(lID);

            if (refreshedToken != null) {
                myRef.child(refreshedToken).child("name").setValue(name);
                myRef.child(refreshedToken).child("email").setValue(email);
            }

            googleApiClient.connect();
            updateUi(true);
        } else {
            updateUi(false);
        }
    }

    private void updateUi(boolean IsLogin) {
        if (IsLogin) {
            GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
            mSignIn_Navigation.setText(lGEUserManager.getmUserInfo().getUserEmail());
            mWelcom_SignIn.setText(lGEUserManager.getmUserInfo().getUserName());
            Glide.with(this).load(lGEUserManager.getmUserInfo().getmUserImageUrl()).into(mUserIv);
        } else {
            mWelcom_SignIn.setText("Welcome");
            mSignIn_Navigation.setText("SignIn");
            try {
                InputStream lInputStream = getAssets().open("images/userprofile.png");
                Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
                SharedPreferences sharedPreferences = getSharedPreferences("myTheme", MODE_PRIVATE);
                GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition", 0));
                int lColor = GEThemeManager.getInstance(this).getSelectedNavTextColor();
                changeBitmapColor(lBitmap, mUserIv, lColor);
            } catch (IOException e) {
//            handle exception
            }
        }
    }

    private void changeBitmapColor(Bitmap sourceBitmap, ImageView image, int color) {
        Bitmap resultBitmap = Bitmap.createBitmap(sourceBitmap, 0, 0,
                sourceBitmap.getWidth() - 1, sourceBitmap.getHeight() - 1);
        Paint p = new Paint();
        ColorFilter filter = new PorterDuffColorFilter(color, PorterDuff.Mode.SRC_IN);
        p.setColorFilter(filter);
        image.setImageBitmap(resultBitmap);
        Canvas canvas = new Canvas(resultBitmap);
        canvas.drawBitmap(resultBitmap, 0, 0, p);
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

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
