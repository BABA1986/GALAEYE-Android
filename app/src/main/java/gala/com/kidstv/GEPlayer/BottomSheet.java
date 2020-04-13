package gala.com.kidstv.GEPlayer;

import android.annotation.SuppressLint;
import gala.com.kidstv.GETheme.GEThemeManager;
import gala.com.kidstv.R;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffColorFilter;
import android.graphics.Typeface;
import androidx.annotation.NonNull;
import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * Created by admin on 30/12/17.
 */

public class BottomSheet extends BottomSheetDialog implements View.OnClickListener {

    @SuppressLint("StaticFieldLeak")
    private final Context context;
    private ImageView mShareLink;
    private TextView  mTextView;
    private TextView  mSubTitleTextView;
    private View bottomSheetView;
    private BottomSheetInterFace    mInterface;

    public BottomSheet(@NonNull Context context, BottomSheetInterFace aInterface) {
        super(context);
        this.context = context;
        mInterface = aInterface;
        create();
    }

    public void create() {
        bottomSheetView = getLayoutInflater().inflate(R.layout.bottom_sheet, null);
        bottomSheetView.setOnClickListener(this);
        setContentView(bottomSheetView);
        final BottomSheetBehavior bottomSheetBehavior = BottomSheetBehavior.from((View) bottomSheetView.getParent());
        this.setOnShowListener(new OnShowListener() {
            @Override
            public void onShow(DialogInterface dialog) {
                bottomSheetBehavior.setPeekHeight(bottomSheetView.getHeight());
            }
        });

        BottomSheetBehavior.BottomSheetCallback bottomSheetCallback = new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                // do something
                if (newState == BottomSheetBehavior.STATE_HIDDEN){
                    mInterface.onDissmissBottomSheet(BottomSheet.this);
                }
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

        mTextView = (TextView)bottomSheetView.findViewById(R.id.tv_title);
        mTextView.setText(mInterface.textIfRequiredOnSheet(this));

        mSubTitleTextView = (TextView)bottomSheetView.findViewById(R.id.tv_subtitle);

        mSubTitleTextView.setText("Share With your friends");
        Typeface mtypeFace = Typeface.createFromAsset(context.getAssets(),
                "fonts/Muli-SemiBold.ttf");
        // set TypeFace to the TextView or Edittext
        mSubTitleTextView.setTypeface(mtypeFace);
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
        this.dismiss();
        mInterface.shareClickFoundOn(BottomSheet.this);
    }
}
