package gala.com.kidstv.Refactored.items.PosterItems;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.bitmap_recycle.BitmapPool;
import com.bumptech.glide.load.resource.bitmap.BitmapTransformation;
import com.github.vivchar.rendererrecyclerviewadapter.ViewRenderer;

import java.security.MessageDigest;

import gala.com.kidstv.R;
import gala.com.kidstv.Refactored.CallBackListener;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaModel;
import gala.com.kidstv.Refactored.items.MediaInfo.MediaRenderer;

public class PosterRenderer extends ViewRenderer<PosterModel, PosterViewHolder> {

    private final Context mContext;
    @NonNull
    private CallBackListener mListener;

    public PosterRenderer(Context context, CallBackListener listener) {
        super(PosterModel.class);
        mContext = context;
        mListener = listener;
    }

    @Override
    public void bindView(@NonNull final PosterModel model, @NonNull final PosterViewHolder holder) {
        if (model.getMediaLargeIcon()!=null){
            Glide.with(mContext).asBitmap().load(model.getMediaLargeIcon())
                    .transform(new MyTransformation(mContext)).override(120, 140).into(holder.mIvPoster);
            holder.mIvPoster.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onCategoryClicked(model);
                }
            });
        }
    }

    @NonNull
    @Override
    public PosterViewHolder createViewHolder(@Nullable final ViewGroup parent) {
        return new PosterViewHolder(inflate(R.layout.poster_view_holder, parent));
    }

    private static class MyTransformation extends BitmapTransformation {

        public MyTransformation(Context context) {
            super();
        }

        @Override
        protected Bitmap transform(@NonNull BitmapPool pool, @NonNull Bitmap toTransform,
                                   int outWidth, int outHeight) {
            return transformBitmap(pool, toTransform, outWidth, outHeight);
        }

        protected Bitmap transformBitmap(BitmapPool bitmapPool, Bitmap original, int width, int height) {
            Bitmap result = bitmapPool.get(width, height, Bitmap.Config.ARGB_8888);
            // If no matching Bitmap is in the pool, get will return null, so we should allocate.
            if (result == null) {
                // Use ARGB_8888 since we're going to add alpha to the image.
                result = Bitmap.createBitmap(width, height, Bitmap.Config.ARGB_8888);
            }
            // Create a Canvas backed by the result Bitmap.
            Canvas canvas = new Canvas(result);
            Paint paint = new Paint();
            paint.setAlpha(255);
            // Draw the original Bitmap onto the result Bitmap with a transformation.
            int bw=original.getWidth();
            int bh=original.getHeight();

            int cx = (bw - width) / 2;
            int cy = (bh - height) / 2;

            canvas.drawBitmap(original, -cx, -cy, paint);
            // Since we've replaced our original Bitmap, we return our new Bitmap here. Glide will
            // will take care of returning our original Bitmap to the BitmapPool for us.
            return result;
        }

        @Override
        public void updateDiskCacheKey(@NonNull MessageDigest messageDigest) {

        }
    }
}
