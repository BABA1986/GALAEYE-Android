package gala.com.kidstv.GEPlayer;

import com.google.android.youtube.player.YouTubeInitializationResult;

/**
 * Created by deepak on 10/12/17.
 */

public interface GEVideoPlayerViewListner {
    public void onInitializationSuccess(GEVideoPlayerView playerView, boolean b);
    public void onInitializationFailure(GEVideoPlayerView playerView, YouTubeInitializationResult youTubeInitializationResult);
    public void onEnterToFullScreen(boolean b);
    public void openSheet();
}
