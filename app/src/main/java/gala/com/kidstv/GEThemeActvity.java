package gala.com.kidstv;

import gala.com.kidstv.GETheme.GEThemeManager;
import android.content.Context;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import androidx.viewpager.widget.PagerAdapter;
import androidx.viewpager.widget.ViewPager;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.appcompat.widget.Toolbar;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import uk.co.chrisjenx.calligraphy.CalligraphyContextWrapper;

public class GEThemeActvity extends AppCompatActivity {

    private ViewPager viewPager;
    private LinearLayout dotsLayout;
    private Button btnNext;
    private GEThemePageAdapter myViewPagerAdapter;
    private TextView[] dots;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_getheme_actvity);
        Toolbar lToolBar = (Toolbar)findViewById(R.id.toolbar);
        setSupportActionBar(lToolBar);
        getSupportActionBar().setElevation(0);
        getSupportActionBar().setTitle("");
        viewPager = (ViewPager) findViewById(R.id.view_pager);
        dotsLayout = (LinearLayout) findViewById(R.id.layoutDots);
        btnNext = (Button) findViewById(R.id.btn_next);

        TextView lTitleBar = (TextView)lToolBar.findViewById(R.id.toolbar_title);
        lTitleBar.setText("Theme");
        lTitleBar.setTypeface(Typeface.createFromAsset(getAssets(), "fonts/wicked.otf"));

        // layouts of all GETheme
        // add few more layouts if you want


        // adding bottom dots
        addBottomDots(0);
        myViewPagerAdapter = new GEThemePageAdapter(this);
        viewPager.setAdapter(myViewPagerAdapter);
        viewPager.addOnPageChangeListener(viewPagerPageChangeListener);
        viewPager.setCurrentItem(GEThemeManager.getInstance(this).getmSelectedIndex());
        applyTheme();

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // checking for last page
                // if last page home screen will be launched
                int current = getItem(+1);
                int lThemeCount = GEThemeManager.getInstance(getBaseContext()).getThemesCount();
                if (current < lThemeCount) {
                    // move to next screen
                    viewPager.setCurrentItem(current);
                }
            }
        });
    }

    private void applyTheme()
    {
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
        for (int i = 0; i < dots.length; i++) {
            dots[i].setTextColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
    }

    private void addBottomDots(int currentPage) {
        int lThemeCount = GEThemeManager.getInstance(getBaseContext()).getThemesCount();
        dots = new TextView[lThemeCount];
        dotsLayout.removeAllViews();
        for (int i = 0; i < dots.length; i++) {
            dots[i] = new TextView(this);
            dots[i].setText(Html.fromHtml("&#8226;"));
            dots[i].setTextSize(35);
            dotsLayout.addView(dots[i]);
        }

        if (dots.length > 0)
            dots[currentPage].setTextColor(GEThemeManager.getInstance(this).getSelectedNavTextColor());
    }

    private int getItem(int i) {
        return viewPager.getCurrentItem() + i;

    }

    //  viewpager change listener
    ViewPager.OnPageChangeListener viewPagerPageChangeListener = new ViewPager.OnPageChangeListener() {

        @Override
        public void onPageSelected(int position) {
            addBottomDots(position);
            GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(position);
            int lThemeCount = GEThemeManager.getInstance(getBaseContext()).getThemesCount();
            if (position == lThemeCount - 1) {
                btnNext.setVisibility(View.GONE);
            }

            else if(position==lThemeCount-2){
                btnNext.setVisibility(View.GONE);
            }

            applyTheme();
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {

        }

        @Override
        public void onPageScrollStateChanged(int arg0) {

        }
    };

    /**
     * View pager adapter
     */
    public class GEThemePageAdapter extends PagerAdapter {
        private LayoutInflater layoutInflater;
        private Context                 mContext;

        public GEThemePageAdapter(Context context) {
            mContext = context;
        }

        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            layoutInflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            View view = layoutInflater.inflate(R.layout.ge_theme1, container, false);
            TextView lTextView = (TextView)view.findViewById(R.id.themenametextviewid);
            lTextView.setText("KidsTV");
            RelativeLayout lHeaderlout=(RelativeLayout)view.findViewById(R.id.header_lout);
            GEThemeManager lThemeManager = GEThemeManager.getInstance(mContext);
            lTextView.setTextColor(lThemeManager.getSelectedNavTextColorAtIndex(position));
            lHeaderlout.setBackgroundColor(lThemeManager.getNavColorAtIndex(position));
            container.addView(view);
            return view;
        }

        @Override
        public int getCount()
        {
            GEThemeManager lThemeManager = GEThemeManager.getInstance(mContext);
            int lThemesCount = lThemeManager.getThemesCount();
            return lThemesCount;
        }

        @Override
        public boolean isViewFromObject(View view, Object obj) {
            return view == obj;
        }


        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            View view = (View) object;
            container.removeView(view);
        }
    }

    @Override
    protected void attachBaseContext(Context newBase) {
        super.attachBaseContext(CalligraphyContextWrapper.wrap(newBase));
    }
}
