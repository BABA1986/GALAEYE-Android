package android.com.galatube.model.GEMenu;

import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.R;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.LightingColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.PorterDuffXfermode;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
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
            convertView = mInflater.inflate(R.layout.ge_menu_list_adapter, parent, false);
            lHolder = new ViewHolder();
            lHolder.mTitleView = (TextView) convertView.findViewById(R.id.adapter_title_view);
            lHolder.mImageView = (ImageView) convertView.findViewById(R.id.adapter_icon_view);
            convertView.setTag(lHolder);
        } else {
            lHolder = (ViewHolder) convertView.getTag();
        }

        GEMenu lMenuInfo = mMenuIems.get(position);
        lHolder.mTitleView.setText(lMenuInfo.getmMenuName());

        try {
            InputStream lInputStream = mContext.getAssets().open("images/gala_icon_xxhdpi.png");
            Bitmap lBitmap = BitmapFactory.decodeStream(lInputStream);
            lHolder.mImageView.setImageBitmap(lBitmap);

            SharedPreferences sharedPreferences = mContext.getSharedPreferences("myTheme", Context.MODE_PRIVATE);
            GEThemeManager.getInstance(mContext).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
            int lColor = GEThemeManager.getInstance(mContext).getSelectedNavColor();
            changeBitmapColor(lBitmap, lHolder.mImageView, lColor);
        } catch (IOException e) {
//            handle exception
        }

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
