package android.com.galatube.GEPlayer;

import android.com.galatube.GEYoutubeEvents.GEDateUtils;
import android.com.galatube.GEYoutubeEvents.GENumFormatter;
import android.com.galatube.R;
import android.content.Context;
import android.net.Uri;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.api.client.util.DateTime;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.Video;
import com.nostra13.universalimageloader.core.ImageLoader;

import java.io.File;
import java.math.BigInteger;
import java.text.NumberFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Created by deepak on 02/12/17.
 */

public class GEVideoDetailView extends LinearLayout implements View.OnClickListener{
    ImageView           mThumbnailView;
    TextView            mChannelTitleView;
    TextView            mChannelSubTitleView;

    TextView            mVideoTitleView;
    TextView            mVideoSubTitleView;
    TextView            mVideoPublishOnView;
    TextView            mVideoDescView;
    ImageView           mExpandCollapseView;
    boolean             mExpanded;

    public GEVideoDetailView(Context context) {
        super(context);
        init();
    }

    public GEVideoDetailView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public GEVideoDetailView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        View view = inflate(getContext(), R.layout.video_detail_layout, this);
        view.setOnClickListener(this);

        mThumbnailView = (ImageView) view.findViewById(R.id.channelthumbicon);
        mExpandCollapseView = (ImageView) view.findViewById(R.id.expandcollapse);
        mChannelTitleView = (TextView) view.findViewById(R.id.channelName);
        mChannelSubTitleView = (TextView) view.findViewById(R.id.subscriptiontextviewid);

        mVideoTitleView = (TextView) view.findViewById(R.id.video_title);
        mVideoSubTitleView = (TextView) view.findViewById(R.id.views_likes_dislikes);
        mVideoPublishOnView = (TextView) view.findViewById(R.id.pulished_at);
        mVideoDescView = (TextView) view.findViewById(R.id.description);
    }

    public void refreshWithInfo(Video videoInfo, Channel channelInfo)
    {
        BigInteger lSubscriptions = channelInfo.getStatistics().getSubscriberCount();
        NumberFormat nf = NumberFormat.getInstance(new Locale("en", "in"));
        String lSubscriptionsStr = nf.format(lSubscriptions);
        String lTitle = channelInfo.getSnippet().getTitle();
        mChannelTitleView.setText(lTitle);
        mChannelSubTitleView.setText(lSubscriptionsStr);

        String lBannerUrl = channelInfo.getBrandingSettings().getImage().getBannerMobileImageUrl();
        String lThumbUrl = channelInfo.getSnippet().getThumbnails().getHigh().getUrl();
        ImageLoader lImageLoader = ImageLoader.getInstance();
        File lThumbfile = ImageLoader.getInstance().getDiskCache().get(lThumbUrl);
        if (lThumbfile == null) {
            //Load image from network
            lImageLoader.displayImage(lThumbUrl, mThumbnailView);
        } else {
            //Load image from cache
            mThumbnailView.setImageURI(Uri.parse(lThumbfile.getAbsolutePath()));
        }

        int lViewCount = videoInfo.getStatistics().getViewCount().intValue();
        int lLikeCount = videoInfo.getStatistics().getLikeCount().intValue();
        int lDisLikeCount = videoInfo.getStatistics().getDislikeCount().intValue();

        String lViewCountStr = GENumFormatter.format(lViewCount);
        String lLikeCountStr = GENumFormatter.format(lLikeCount);

        String lVideoTitleStr = videoInfo.getSnippet().getTitle();
        mVideoTitleView.setText(lVideoTitleStr);
        String lDetail = lViewCountStr + " Views " + "•" + lLikeCountStr + " Likes " + "•" + lDisLikeCount+ " Dislikes ";
        mVideoSubTitleView.setText(lDetail);

        DateTime lPublishedOn = videoInfo.getSnippet().getPublishedAt();
        long lTimeStamp = lPublishedOn.getValue();
        long lCurrTimeStamp = new Date().getTime();
        long lDeff = lCurrTimeStamp - lTimeStamp;
        String lAgoString = GEDateUtils.getTimeAgo(lDeff);
        mVideoPublishOnView.setText(lAgoString);

        String lDescStr = videoInfo.getSnippet().getDescription();
        mVideoDescView.setText(lDescStr);
    }

    @Override
    public void onClick(View v) {
        expandCollapse();
    }

    private void expandCollapse()
    {
        LinearLayout lLayout = (LinearLayout)findViewById(R.id.descriptionbaseview);
        TranslateAnimation anim = null;
        if (mExpanded) {
            mExpandCollapseView.animate().rotation(180).start();
            lLayout.setVisibility(GONE);
            mExpanded = false;
        }
        else {
            mExpandCollapseView.animate().rotation(0).start();
            lLayout.setVisibility(VISIBLE);
            mExpanded = true;
        }
    }
}
