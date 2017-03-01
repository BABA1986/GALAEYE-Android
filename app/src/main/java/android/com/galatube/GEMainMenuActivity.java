package android.com.galatube;

import android.com.galatube.model.GEMenu.GEMenu;
import android.com.galatube.model.GEMenu.GEMenuAdapter;
import android.com.galatube.model.GEMenu.GESharedMenu;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutCompat;
import android.support.v7.widget.Toolbar;
import android.text.Layout;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;

import java.util.ArrayList;

/**
 * Created by deepak on 30/11/16.
 */

public class GEMainMenuActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    DrawerLayout            mDrawer;
    private LinearLayout mSettingLayout;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mSettingLayout=(LinearLayout)findViewById(R.id.setting_base_adapter);
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
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

        initialisePagesForMenu(lMenuItems.get(0));
        mSettingLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(GEMainMenuActivity.this,SettingActivity.class);
                startActivity(intent);
            }
        });
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
}
