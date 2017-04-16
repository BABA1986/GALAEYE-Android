package android.com.galatube;

import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.GEUserModal.GEUserManager;
import android.com.galatube.Connectivity.GENetworkState;
import android.com.galatube.model.GEMenu.GEMenu;
import android.com.galatube.model.GEMenu.GEMenuAdapter;
import android.com.galatube.model.GEMenu.GESharedMenu;
import android.com.galatube.model.GEMenu.GESubMenu;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.Scopes;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.Scope;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

/**
 * Created by deepak on 30/11/16.
 */

public class GEMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener, GoogleApiClient.ConnectionCallbacks{

    DrawerLayout            mDrawer;
    private LinearLayout mSettingLayout;
    private LinearLayout mGoogleNavigationSignIn;
    private TextView mWelcom_SignIn;
    private TextView mSignIn_Navigation;
    private ImageView mUserIv;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;
    private LinearLayout mGooGleHeader;
    private Toolbar mtoolbar;
    private LinearLayout mTabToolbar;
    private Menu         mActionBarMenu;
    private int          mDrawerSelectedMenuIndex;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
         mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        mGoogleNavigationSignIn=(LinearLayout)findViewById(R.id.Google_Navigation_Header);
        mTabToolbar=(LinearLayout)findViewById(R.id.tab_toolbar);
        mWelcom_SignIn=(TextView)findViewById(R.id.welcome_tv);
        mSignIn_Navigation=(TextView)findViewById(R.id.signIn_tv);
        mUserIv=(ImageView)findViewById(R.id.imageView);
        mUserIv.setOnClickListener(this);
        mSettingLayout=(LinearLayout)findViewById(R.id.setting_base_adapter);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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
        ListView lListView = (ListView) findViewById(R.id.left_drawer_list);
        lListView.setAdapter(new GEMenuAdapter(this, lMenuItems));
        lListView.setOnItemClickListener(new AdapterView.OnItemClickListener()
        {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id)
            {
                mDrawerSelectedMenuIndex = position;
                mDrawer.closeDrawers();
                ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
                initialisePagesForMenu(lMenuItems.get(position), "playlists");
            }
        });
        GEUserManager lGEUsermanager=GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0)
        {
            updateUi(true);
        }

        mDrawerSelectedMenuIndex = 0;
        initialisePagesForMenu(lMenuItems.get(mDrawerSelectedMenuIndex), "playlists");
//        GoogleSignInOptions signInOptions = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().requestProfile()
//                .requestServerAuthCode("820754345275-ata3ks9k1lj6e28k0oqju40a3cmoem4n.apps.googleusercontent.com")
//                .requestScopes(
//                new Scope("https://www.googleapis.com/auth/youtube"),
//                new Scope("https://www.googleapis.com/auth/youtube.upload"))
//                .build();
//        googleApiClient = new GoogleApiClient.Builder(this)
//                .enableAutoManage(this, this)
//                .addApi(Auth.GOOGLE_SIGN_IN_API, signInOptions)
//                .build();

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
                // .addApi(Plus.API, null)
                .addConnectionCallbacks((GoogleApiClient.ConnectionCallbacks) this)
                .addOnConnectionFailedListener(this)
                // .addScope(Plus.SCOPE_PLUS_LOGIN)
                .build();

    }
    private void applyTheme() {
        SharedPreferences sharedPreferences = getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        ActionBar lActionBar = getSupportActionBar();
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        mGoogleNavigationSignIn.setBackgroundColor(lColor);
        mTabToolbar.setBackgroundColor(lColor);
        mtoolbar.setBackgroundColor(lColor);
        ColorDrawable lColorDrawable = new ColorDrawable(lColor);
        lActionBar.setBackgroundDrawable(lColorDrawable);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
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

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item)
    {
        ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
        GEMenu lMenu = lMenuItems.get(mDrawerSelectedMenuIndex);

        switch (item.getItemId())
        {
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
    private void showOptionMenuIfRequired()
    {
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

    public void initialisePagesForMenu(GEMenu menuInfo, String filter)
    {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        GEPageAdapter lAdapter = (GEPageAdapter)viewPager.getAdapter();
        showOptionMenuIfRequired();
        if (lAdapter != null)
        {
            lAdapter.reloadWithSubMenu(menuInfo.getmSubMenus(), filter);
            return;
        }

        viewPager.setAdapter(new GEPageAdapter(getSupportFragmentManager(),
                GEMainMenuActivity.this, menuInfo.getmSubMenus(), filter));
        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        SharedPreferences sharedPreferences = getSharedPreferences("myTheme", MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lTextColor=GEThemeManager.getInstance(this).getSelectedNavTextColor();
        tabLayout.setSelectedTabIndicatorColor(lTextColor);
        tabLayout.setupWithViewPager(viewPager);
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


    public void signIn(){
        Intent intent = Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
        startActivityForResult(intent,REQ_CODE);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode==REQ_CODE){
            GoogleSignInResult result=Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleResult(result);
        }
    }

    private void handleResult(GoogleSignInResult result) {
        if (result.isSuccess()){
            GoogleSignInAccount account = result.getSignInAccount();
            String lAccessToken = (account != null) ? account.getServerAuthCode() : "";
            String name = account.getDisplayName();
            String email = account.getEmail();
            String image = account.getPhotoUrl().toString();
            GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
            lGEUserManager.setUserName(name);
            lGEUserManager.setUserEmail(email);
            lGEUserManager.setUserImageUrl(image);
            updateUi(true);
        }
        else {
            updateUi(false);
        }
    }

    private void updateUi(boolean IsLogin) {
        if (IsLogin){
            GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
            mSignIn_Navigation.setText(lGEUserManager.getmUserInfo().getUserEmail());
            mWelcom_SignIn.setText(lGEUserManager.getmUserInfo().getUserName());
            Glide.with(this).load(lGEUserManager.getmUserInfo().getmUserImageUrl()).into(mUserIv);
        }else{
            mWelcom_SignIn.setText("Welcome");
            mSignIn_Navigation.setText("SignIn");
            try {
                InputStream lInputStream = getAssets().open("images/userprofile.png");
                Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
                mUserIv.setImageBitmap(lBitmap);
            }
            catch (IOException e) {
//            handle exception
            }
        }
    }
    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }


    @Override
    protected void onStart() {
        super.onStart();
        applyTheme();
        GEUserManager lGEUsermanager=GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0)
        {
            updateUi(true);
        }else {
            updateUi(false);
        }
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }
}
