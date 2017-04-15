package android.com.galatube;

import android.com.galatube.GETheme.GEThemeManager;
import android.com.galatube.GEYoutubeEvents.*;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Toast;

import com.google.android.youtube.player.YouTubeBaseActivity;
import com.google.android.youtube.player.YouTubeInitializationResult;
import com.google.android.youtube.player.YouTubePlayer;
import com.google.android.youtube.player.YouTubePlayerView;

public class GEEventPlayActivity extends YouTubeBaseActivity implements
        YouTubePlayer.OnInitializedListener {

    private Toolbar mToolbar;
    private RecyclerView mAllevent;
    private LinearLayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geevent_play);
        mToolbar=(Toolbar)findViewById(R.id.GEtoobar);
        mAllevent=(RecyclerView)findViewById(R.id.AllEvent);
        mLayoutManager = new LinearLayoutManager(this);
        mAllevent.setLayoutManager(mLayoutManager);
        GEPlayeventAdapter lAdapter = new GEPlayeventAdapter(this);
        mAllevent.setAdapter(lAdapter);
        applyTheme();
        YouTubePlayerView lYoutubePlayerView=(YouTubePlayerView)findViewById(R.id.youtube_view);
        lYoutubePlayerView.initialize(GEConstants.GEAPIKEY,this);

    }

    private void applyTheme() {
        SharedPreferences sharedPreferences=getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(getBaseContext()).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(this).getSelectedNavColor();
        mToolbar.setBackgroundColor(lColor);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Window window = getWindow();
            window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            window.setStatusBarColor(GEThemeManager.getInstance(this).getSelectedNavColor());
        }
    }

    @Override
    public void onInitializationSuccess(YouTubePlayer.Provider provider, YouTubePlayer youTubePlayer, boolean b) {
           youTubePlayer.setPlayerStateChangeListener(playerStateChangeListener);
           youTubePlayer.setPlaybackEventListener(playbackEventListener);
        if (!b){
            youTubePlayer.cueVideo("YyKQ6uvFLtw");
        }
    }

    private YouTubePlayer.PlaybackEventListener playbackEventListener= new YouTubePlayer.PlaybackEventListener() {
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

    private YouTubePlayer.PlayerStateChangeListener playerStateChangeListener=new YouTubePlayer.PlayerStateChangeListener() {
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

        }

        @Override
        public void onVideoEnded() {

        }

        @Override
        public void onError(YouTubePlayer.ErrorReason errorReason) {

        }
    };




    @Override
    public void onInitializationFailure(YouTubePlayer.Provider provider, YouTubeInitializationResult youTubeInitializationResult) {

        Toast.makeText(this, "Failured to Initialize", Toast.LENGTH_SHORT).show();
    }


}
