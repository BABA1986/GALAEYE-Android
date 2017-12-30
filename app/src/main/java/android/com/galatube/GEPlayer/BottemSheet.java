package android.com.galatube.GEPlayer;

import android.annotation.SuppressLint;
import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.GEYoutubeEvents.GEEventManager;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.GEYoutubeEvents.GEVideoListObj;
import android.com.galatube.GEYoutubeEvents.GEVideoListPage;
import android.com.galatube.R;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialog;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.api.services.youtube.model.Video;
import com.google.firebase.dynamiclinks.DynamicLink;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.ShortDynamicLink;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by admin on 30/12/17.
 */

public class BottemSheet extends BottomSheetDialog implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private static BottemSheet instance;
    private final Context context;
    private ImageView mShareLink;

    public BottemSheet(@NonNull Context context) {
        super(context);
        this.context = context;
        create();
    }

    public static BottemSheet getInstance(@NonNull Context context) {
        return instance == null ? new BottemSheet(context) : instance;
    }

    public void create() {
        View bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        setContentView(bottomSheetView);
        BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        //bottomSheetBehavior.setPeekHeight(bottomSheetView.getHeight());
        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // do something
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                // do something
            }
        };
        mShareLink=(ImageView)bottomSheetView.findViewById(R.id.image);
        Resources resources = getContext().getResources();
        int resourceId =resources.getIdentifier("forwardarrow", "drawable",
                getContext().getPackageName());
        Bitmap lBitmap = BitmapFactory.decodeResource(getContext().getResources(),
                resourceId);
        mShareLink.setImageBitmap(lBitmap);
        int lColor = GEThemeManager.getInstance(getContext()).getSelectedNavColor();
        mShareLink.setColorFilter(lColor);
       // changeBitmapColor(lBitmap, mShareLink, lColor);
    }

    private void changeBitmapColor(Bitmap sourceBitmap, ImageView image, int color)
    {
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
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.image:
                //shareDynamicLink();
                break;
        }
    }
}
