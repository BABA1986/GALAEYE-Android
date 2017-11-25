package android.com.galatube.GEYoutubeEvents;

import com.google.api.services.youtube.model.Channel;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by deepak on 14/11/17.
 */

public class GEChannelManager {
    private static GEChannelManager ourInstance = new GEChannelManager();

    public static GEChannelManager getInstance() {
        return ourInstance;
    }

    private HashMap<String, Channel> mChannelsInfo;

    private GEChannelManager() {
        mChannelsInfo = new HashMap<String, Channel>();
    }

    public void addChnnelWithName(String channelName, Channel channel){
        mChannelsInfo.put(channelName, channel);
    }

    public Channel channelWithName(String channelName){
        Channel lChannel = mChannelsInfo.get(channelName);
        return lChannel;
    }
}
