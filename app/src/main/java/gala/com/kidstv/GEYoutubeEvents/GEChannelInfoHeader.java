package gala.com.kidstv.GEYoutubeEvents;

import gala.com.kidstv.R;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;

/**
 * Created by deepak on 16/11/17.
 */

public class GEChannelInfoHeader extends RelativeLayout {

    ImageView           mBannerView;
    ImageView           mThumbnailView;
    TextView            mTitleView;
    TextView            mSubTitleView;

    public GEChannelInfoHeader(Context context) {
        super(context);
        init();
    }

    public GEChannelInfoHeader(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GEChannelInfoHeader(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.channel_header_layout, this);
        mTitleView = (TextView) view.findViewById(R.id.channelName);
        mSubTitleView = (TextView) view.findViewById(R.id.subscriptiontextviewid);

        mBannerView = (ImageView) view.findViewById(R.id.imageblur);
        mThumbnailView = (ImageView) view.findViewById(R.id.channelthumbicon);
    }

    public void refreshWithInfo(String imageUrl, String thumbUrl, String title, String subtitle)
    {
        mTitleView.setText(title);
        if (subtitle.equals("")) mSubTitleView.setVisibility(GONE);
        else  mSubTitleView.setVisibility(VISIBLE);
        mSubTitleView.setText(subtitle + " " + "subscribers");
        String lUrl = imageUrl;
        String lThumbUrl = thumbUrl;
        ImageLoader lImageLoader = ImageLoader.getInstance();
        File file = null;
        File lThumbfile = null;
        if(lUrl != null)
            file = ImageLoader.getInstance().getDiskCache().get(lUrl);

        if(lThumbUrl != null)
            lThumbfile = ImageLoader.getInstance().getDiskCache().get(lThumbUrl);

        if (file == null) {
            //Load image from network
            lImageLoader.displayImage(lUrl, mBannerView);
        } else {
            //Load image from cache
            mBannerView.setImageURI(Uri.parse(file.getAbsolutePath()));
        }
        if (lThumbfile == null) {
            //Load image from network
            lImageLoader.displayImage(lThumbUrl, mThumbnailView);
        } else {
            //Load image from cache
            mThumbnailView.setImageURI(Uri.parse(lThumbfile.getAbsolutePath()));
        }

    }
}
