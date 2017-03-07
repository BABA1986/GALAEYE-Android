package android.com.galatube;

import android.com.galatube.GEUserModal.GEUserManager;
import android.com.galatube.Connectivity.GENetworkState;
import android.com.galatube.model.GEMenu.GEMenu;
import android.com.galatube.model.GEMenu.GEMenuAdapter;
import android.com.galatube.model.GEMenu.GESharedMenu;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
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

public class GEMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener,View.OnClickListener,GoogleApiClient.OnConnectionFailedListener{

    DrawerLayout            mDrawer;
    private LinearLayout mSettingLayout;
    private LinearLayout mGoogleNavigationSignIn;
    private TextView mWelcom_SignIn;
    private TextView mSignIn_Navigation;
    private ImageView mUserIv;
    private GoogleApiClient googleApiClient;
    private static final int REQ_CODE=9001;
    private LinearLayout mGooGleHeader;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setElevation(0);
        mGoogleNavigationSignIn=(LinearLayout)findViewById(R.id.Google_Navigation_Header);
        mWelcom_SignIn=(TextView)findViewById(R.id.welcome_tv);
        mSignIn_Navigation=(TextView)findViewById(R.id.signIn_tv);
        mUserIv=(ImageView)findViewById(R.id.imageView);
        mUserIv.setOnClickListener(this);
        mSettingLayout=(LinearLayout)findViewById(R.id.setting_base_adapter);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mSettingLayout.setOnClickListener(this);
        mGoogleNavigationSignIn.setOnClickListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
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
        lListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                mDrawer.closeDrawers();
                ArrayList<GEMenu> lMenuItems = GESharedMenu.getInstance(getApplicationContext()).getMenus();
                initialisePagesForMenu(lMenuItems.get(position));
            }
        });
        GEUserManager lGEUsermanager=GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0)
        {
            updateUi(true);
        }
        initialisePagesForMenu(lMenuItems.get(0));
        GoogleSignInOptions signInOptions=new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN).requestEmail().build();
        googleApiClient=new GoogleApiClient.Builder(this).enableAutoManage(this,this).addApi(Auth.GOOGLE_SIGN_IN_API,signInOptions).build();

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

    public void initialisePagesForMenu(GEMenu menuInfo)
    {
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewpager);
        GEPageAdapter lAdapter = (GEPageAdapter)viewPager.getAdapter();

        if (lAdapter != null)
        {
            lAdapter.reloadWithSubMenu(menuInfo.getmSubMenus());
            return;
        }
        viewPager.setAdapter(new GEPageAdapter(getSupportFragmentManager(),
                GEMainMenuActivity.this, menuInfo.getmSubMenus()));

        // Give the TabLayout the ViewPager
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
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
        Intent intent=Auth.GoogleSignInApi.getSignInIntent(googleApiClient);
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
            GoogleSignInAccount account=result.getSignInAccount();
            String name=account.getDisplayName();
            String email=account.getEmail();
            String image =account.getPhotoUrl().toString();
            GEUserManager lGEUserManager = GEUserManager.getInstance(getApplicationContext());
            lGEUserManager.setUserName(name);
            lGEUserManager.setUserEmail(email);
            lGEUserManager.setUserImageUrl(image);
            updateUi(true);
        }
        else
        {
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
        GEUserManager lGEUsermanager=GEUserManager.getInstance(getApplicationContext());
        if (lGEUsermanager.getmUserInfo().getUserEmail().length() != 0)
        {
            updateUi(true);
        }else {
            updateUi(false);
        }
    }


}
