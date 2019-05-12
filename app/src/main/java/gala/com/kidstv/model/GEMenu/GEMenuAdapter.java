package gala.com.kidstv.model.GEMenu;

import gala.com.kidstv.GETheme.GEThemeManager;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.StateListDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by deepak on 06/12/16.
 */

public class GEMenuAdapter extends BaseAdapter {
    Context mContext;
    ArrayList<GEMenu> mMenuIems;

    private static LayoutInflater mInflater = null;

    public GEMenuAdapter(Context context, ArrayList<GEMenu> menuItems) {
        // TODO Auto-generated constructor stub
        this.mContext = context;
        this.mMenuIems = menuItems;
        mInflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    class ViewHolder {
        public TextView mTitleView;
        public ImageView mImageView;
        public RelativeLayout mImageBaseView;
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return mMenuIems.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return mMenuIems.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder lHolder;
        if (convertView == null) {
            convertView = mInflater.inflate(gala.com.kidstv.R.layout.ge_menu_list_adapter, parent, false);
            lHolder = new ViewHolder();
            lHolder.mTitleView = (TextView) convertView.findViewById(gala.com.kidstv.R.id.adapter_title_view);
            lHolder.mImageView = (ImageView) convertView.findViewById(gala.com.kidstv.R.id.adapter_icon_view);
            lHolder.mImageBaseView = (RelativeLayout) convertView.findViewById(gala.com.kidstv.R.id.imagebaseview);
            convertView.setTag(lHolder);
        } else {
            lHolder = (ViewHolder) convertView.getTag();
        }

        SharedPreferences sharedPreferences = mContext.getSharedPreferences("myTheme", Context.MODE_PRIVATE);
        GEThemeManager.getInstance(mContext).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(mContext).getSelectedNavColor();

        GEMenu lMenuInfo = mMenuIems.get(position);

        StateListDrawable bgShape = (StateListDrawable)lHolder.mImageBaseView.getBackground();
        bgShape.setColorFilter(Color.TRANSPARENT, PorterDuff.Mode.SRC_IN);

        lHolder.mTitleView.setText(lMenuInfo.getmMenuName());
        lHolder.mTitleView.setTextColor(lColor);

        LinearLayout.LayoutParams params = (LinearLayout.LayoutParams)lHolder.mTitleView.getLayoutParams();
        params.setMargins(15, 0, 0, 0);
        lHolder.mTitleView.setLayoutParams(params);

        if (lMenuInfo.isSelected())
        {
            bgShape.setColorFilter(lColor, PorterDuff.Mode.SRC_IN);
            lColor = GEThemeManager.getInstance(mContext).getSelectedNavTextColor();
            params.setMargins(45, 0, 0, 0);
            lHolder.mTitleView.setLayoutParams(params);
        }

        Resources resources = mContext.getResources();
        String lIconName = lMenuInfo.getmMenuImageIcon();
        final int resourceId = resources.getIdentifier(lIconName, "drawable",
                mContext.getPackageName());
        Drawable lDrawable = resources.getDrawable(resourceId);

        Bitmap lBitmap = BitmapFactory.decodeResource(mContext.getResources(),
                resourceId);
        lHolder.mImageView.setImageBitmap(lBitmap);
        changeBitmapColor(lBitmap, lHolder.mImageView, lColor);
        return convertView;
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
}
