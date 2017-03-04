package android.com.galatube.GEYoutubeEvents;

/**
 * Created by deepak on 05/01/17.
 */

public interface GEEventListner {
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success);
    public void playlistsLoadedFromChannel(String channelSource, boolean success);
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success);
}
