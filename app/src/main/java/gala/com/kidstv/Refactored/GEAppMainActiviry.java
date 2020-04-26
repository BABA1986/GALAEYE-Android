package gala.com.kidstv.Refactored;

import android.os.Build;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONException;

import java.util.ArrayList;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.DataHandlers.TabsModel;
import gala.com.kidstv.Refactored.DataHandlers.UTDataManager;
import gala.com.kidstv.Refactored.RoutersAndPresenters.AppPresenter;
import gala.com.kidstv.Refactored.RoutersAndPresenters.AppRouting;
import gala.com.kidstv.Refactored.items.Composite.Category.CategoryModel;

public class GEAppMainActiviry  extends AppCompatActivity implements BottomNavigationView.OnNavigationItemSelectedListener {

    private AppRouting mUIRouter;
    private AppPresenter mPresenter;
    DrawerLayout mDrawer;
    private Toolbar mtoolbar;
    private TextView mToolbarTitle;
    private LinearLayout mTabToolbar;
    private Menu mActionBarMenu;
    private BottomNavigationView mBottomBar;
    private ArrayList<TabsModel> mTabs;
    private UTDataManager mManager;

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ge_refactored_app_main_activity);
        initLayoutElements();
        initToolbar();
        //TODO: NewLayout Related Code
        initRouting(savedInstanceState);

        initTab();
    }

    private void initRouting(Bundle savedInstanceState) {
        mUIRouter = new AppRouting(this);
        final boolean firstInit = savedInstanceState == null;
        mPresenter = new AppPresenter(mUIRouter, firstInit);
    }

    private void initLayoutElements() {
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabToolbar = (LinearLayout) findViewById(R.id.tab_toolbar);
        mBottomBar = (BottomNavigationView) findViewById(R.id.bottombar);
        mBottomBar.setOnNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    private void initToolbar() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("");
        mToolbarTitle = (TextView)mtoolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("KidsTV");
        Window window = GEAppMainActiviry.this.getWindow();
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
        window.clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        window.setStatusBarColor(ContextCompat.getColor(GEAppMainActiviry.this, R.color.colorBlack));
    }

    private void initTab() {
        mManager = UTDataManager.getInstance();
        try {
            mTabs = mManager.initTabs(getApplicationContext());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        if (mTabs!=null && mTabs.size()>0){
            for (int i=0;i<mBottomBar.getMenu().size();i++){
                TabsModel lTabsModel = mTabs.get(i);
                mBottomBar.getMenu().getItem(i).setTitle(lTabsModel.getmTabName());
            }
            initGERecyclerFragment(getCategory(mTabs.get(0)));
        }
    }

    private ArrayList<CategoryModel> getCategory(TabsModel tabsModel) {
        return mManager.getCategoriesFor(tabsModel, getApplicationContext());
    }

    private void initGERecyclerFragment(ArrayList<CategoryModel> categoryList) {
        mPresenter.loadFragment(categoryList);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.home:
                if (mTabs.get(0)!=null){
                    mToolbarTitle.setText("Home");
                    initGERecyclerFragment(getCategory(mTabs.get(0)));
                }
                return true;
            case R.id.channels:
                if (mTabs.get(1)!=null){
                    mToolbarTitle.setText("Channel");
                    initGERecyclerFragment(getCategory(mTabs.get(1)));
                }
                return true;
            case R.id.movies:
                if (mTabs.get(2)!=null){
                    mToolbarTitle.setText("Movies");
                    initGERecyclerFragment(getCategory(mTabs.get(2)));
                }
                return true;
            case R.id.clips:
                if (mTabs.get(3)!=null){
                    mToolbarTitle.setText("Clips");
                    initGERecyclerFragment(getCategory(mTabs.get(3)));
                }
                return true;
            case R.id.profile:
                if (mTabs.get(4)!=null){
                    mToolbarTitle.setText("Profile");
                    initGERecyclerFragment(getCategory(mTabs.get(4)));
                }
                return true;
        }
        return false;
    }
}

