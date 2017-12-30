package gala.com.urtube.GEYoutubeEvents;

import com.google.api.services.youtube.model.Video;

/**
 * Created by deepak on 05/01/17.
 */

public interface GEEventListner {
    public void onYoutubeServicesAuhtenticated();
    public void eventsLoadedFromChannel(String channelID, GEEventTypes eventType, boolean success);
    public void playlistsLoadedFromChannel(String channelSource, boolean success);
    public void playlistsItemsLoadedFromPlaylist(String playlistID, boolean success);

    public void dynamicLinkItemLoaded(Video lVideo, boolean success);
}
