package gala.com.kidstv.Refactored;

import android.os.Bundle;
import android.view.Menu;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.drawerlayout.widget.DrawerLayout;

import org.json.JSONException;

import java.util.ArrayList;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.DataHandlers.TabsModel;
import gala.com.kidstv.Refactored.DataHandlers.UTDataManager;
import gala.com.kidstv.Refactored.RoutersAndPresenters.AppPresenter;
import gala.com.kidstv.Refactored.RoutersAndPresenters.AppRouting;

public class GEAppMainActiviry  extends AppCompatActivity {

    private AppRouting mUIRouter;
    private AppPresenter mPresenter;
    DrawerLayout mDrawer;
    private Toolbar mtoolbar;
    private TextView mToolbarTitle;
    private LinearLayout mTabToolbar;
    private Menu mActionBarMenu;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.ge_refactored_app_main_activity);
        initLayoutElements();
        //TODO: NewLayout Related Code
        initRouting(savedInstanceState);
    }

    private void initRouting(Bundle savedInstanceState) {
        mUIRouter = new AppRouting(this);
        final boolean firstInit = savedInstanceState == null;
        mPresenter = new AppPresenter(mUIRouter, firstInit);
    }

    private void initLayoutElements() {
        mtoolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(mtoolbar);
        getSupportActionBar().setTitle("");
        mToolbarTitle = (TextView)mtoolbar.findViewById(R.id.toolbar_title);
        mToolbarTitle.setText("KidsTV");
        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        mTabToolbar = (LinearLayout) findViewById(R.id.tab_toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mtoolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}

