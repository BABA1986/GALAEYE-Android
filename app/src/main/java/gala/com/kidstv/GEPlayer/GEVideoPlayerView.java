package gala.com.kidstv.GEPlayer;

import gala.com.kidstv.GEConstants;
import gala.com.kidstv.R;
import android.content.Context;
import android.os.Build;
import androidx.annotation.RequiresApi;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.View;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;

import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

import android.os.Handler;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by deepak on 10/12/17.
 */

public class GEVideoPlayerView extends RelativeLayout implements YouTubePlayer.OnInitializedListener {
    private YouTubePlayer               mPlayer;
    private GEVideoPlayerViewListner    mListner;
    private PopupWindow                 mWindow;
    private Handler                     mHandler;

    public GEVideoPlayerView(Context context) {
        super(context);
        initialisePlayer();
    }

    public GEVideoPlayerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initialisePlayer();
    }

    public GEVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initialisePlayer();
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GEVideoPlayerView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        initialisePlayer();
    }

    public void setListner(GEVideoPlayerViewListner listner){
        mListner = listner;
    }

    private void initialisePlayer() {
        View view = inflate(getContext(), R.layout.videoplayerlayout, this);
        mHandler = new Handler();
        YouTubePlayerView lYoutubePlayerView = (YouTubePlayerView) view.findViewById(R.id.youtube_view);
        lYoutubePlayerView.initialize(GEConstants.GEAPIKEY, this);
    }

    public void loadVideo(String videoId) {
        if (mPlayer == null)
            return;

        mHandler.removeCallbacksAndMessages(null);
        mPlayer.loadVideo(videoId);
        mPlayer.play();
        hideWindowControls();
    }

    public void removeListner(){
        mListner = null;
        mHandler.removeCallbacksAndMessages(null);
    }

    public void playVideo() {
        new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                // this code will be executed after 2 seconds
                if (mPlayer != null) {
                    if (!mPlayer.isPlaying())
                        mPlayer.play();
                }
            }
        }, 1000);
    }

    public void pauseVideo() {
        mPlayer.pause();
    }

    public void setPlayerFullScreen(boolean b){
        mPlayer.setFullscreen(b);
        hideWindowControls();
    }

    public void showWindowControls(boolean isFullScreen){
        if (mWindow != null)
            return;

        View popupView = inflate(getContext(), R.layout.geplayerpopwindow, null);
        boolean focusable = false; // lets taps outside the popup also dismiss it
        YouTubePlayerView lYoutubePlayerView = (YouTubePlayerView) this.findViewById(R.id.youtube_view);
        int lHeight = lYoutubePlayerView.getHeight();
        int lWidth = lYoutubePlayerView.getWidth();

        if (isFullScreen)
        {
            mWindow = new PopupWindow(popupView, LayoutParams.MATCH_PARENT,
                    LayoutParams.MATCH_PARENT, focusable);
            mWindow.setOutsideTouchable(false);
            if(Build.VERSION.SDK_INT>=21){
                mWindow.setElevation(5.0f);
            }
            mWindow.showAtLocation(this, Gravity.TOP|Gravity.CENTER, 0, 0);

            return;
        }

        mWindow = new PopupWindow(popupView, lWidth,
                lHeight, focusable);
        mWindow.setOutsideTouchable(false);
        if(Build.VERSION.SDK_INT>=21){
            mWindow.setElevation(5.0f);
        }
        mWindow.showAtLocation(this, Gravity.TOP|Gravity.CENTER, 0, -lHeight);
    }

    public void hideWindowControls(){
        if (mWindow != null) {
//            mWindow.dismiss();
//            mWindow = null;
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
        mPlayer = youTubePlayer;
        mPlayer.setPlayerStyle(YouTubePlayer.PlayerStyle.DEFAULT);
        mPlayer.setFullscreenControlFlags(YouTubePlayer.FULLSCREEN_FLAG_ALWAYS_FULLSCREEN_IN_LANDSCAPE | YouTubePlayer.FULLSCREEN_FLAG_CONTROL_ORIENTATION);

        mPlayer.setOnFullscreenListener(new YouTubePlayer.OnFullscreenListener() {
            @Override
            public void onFullscreen(boolean b) {
                if (mListner != null)
                {
                    final boolean lIsFullScreen = b;
                    mListner.onEnterToFullScreen(b);
                }
            }
        });

        youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
        youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!b) {
            mListner.onInitializationSuccess(this, b);
//            GEInterstitialAdMgr.showInterstitialAd();
        }
    }

    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {
        if (mListner != null)
            mListner.onInitializationFailure(this, youTubeInitializationResult);
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener = new YouTubePlayer.PlaybackEventListener() {
        @Override
        public void onPlaying() {

        }

        @Override
        public void onPaused() {

        }

        @Override
        public void onStopped() {

        }

        @Override
        public void onBuffering(boolean b) {

        }

        @Override
        public void onSeekTo(int i) {
        }
    };

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener = new YouTubePlayer.PlayerStateChangeListener() {
        @Override
        public void onLoading() {

        }

        @Override
        public void onLoaded(String s) {
        }

        @Override
        public void onAdStarted() {

        }

        @Override
        public void onVideoStarted() {
            int lDuration = mPlayer.getDurationMillis();
            int lScheduleAfter = lDuration/2;
            promptShareAfter(lScheduleAfter);
        }

        @Override
        public void onVideoEnded() {
            mListner.openSheet();
        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };

    public void promptShareAfter(int after){
        if (mListner == null)
            return;

        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mListner.openSheet();
            }
        }, after);
    }
}
