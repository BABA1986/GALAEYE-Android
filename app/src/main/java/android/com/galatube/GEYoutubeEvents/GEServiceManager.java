package android.com.galatube.GEYoutubeEvents;

/**
 * Created by deepak on 30/12/16.
 */

import android.accounts.Account;
import android.com.galatube.*;
import android.com.galatube.GEConstants;
import android.com.galatube.GEPlaylist.GEPlaylistManager;
import android.com.galatube.GEPlaylist.GEVideoListManager;
import android.com.galatube.GEUserModal.GEUserManager;
import android.content.Context;
import android.util.Log;

import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.GoogleAccountCredential;
import com.google.api.client.googleapis.extensions.android.gms.auth.UserRecoverableAuthIOException;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.util.Lists;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

public class GEServiceManager
{
    private Context             mContext;
    private YouTube             mYTService;
    private GEEventListner      mListner;;
    private android.os.Handler  mHandler;

    public GEServiceManager(GEEventListner listner, Context context) throws IOException {
        this.mContext = context;
        this.mListner = listner;

        Account lAccount = GEUserManager.getInstance(mContext).getmUserInfo().getGoogleAcct();

        if (lAccount == null) {
            mYTService = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), null).setApplicationName(
                    mContext.getString(R.string.app_name)).build();
//            mListner.onYoutubeServicesAuhtenticated();
            return;
        }

            GoogleAccountCredential lCredential =
                    GoogleAccountCredential.usingOAuth2(
                            mContext,
                            Collections.singleton(
                                    "https://www.googleapis.com/auth/youtube")
                    );

            lCredential.setSelectedAccount(lAccount);
            mYTService = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), lCredential ).setApplicationName(
                    mContext.getString(R.string.app_name)).build();
//            mListner.onYoutubeServicesAuhtenticated();




//        GETokenRefreshTask lRefreshTask = new GETokenRefreshTask(new GETokenRefreshListner() {
//            @Override
//            public Void onCompleteRefreshToken(String newToken) {
//                if (newToken.equals("error"))
//                {
//                    mYTService = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), null).setApplicationName(
//                            mContext.getString(R.string.app_name)).build();
//                    mListner.onYoutubeServicesAuhtenticated();
//                    return null;
//                }
//
//                GoogleCredential lCredential = new GoogleCredential().setAccessToken(newToken);
//                ArrayList scopes = Lists.newArrayList();
//                scopes.add("https://www.googleapis.com/auth/youtube");
//                lCredential .createScoped(scopes);
//                mYTService = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), lCredential ).setApplicationName(
//                        mContext.getString(R.string.app_name)).build();
//                mListner.onYoutubeServicesAuhtenticated();
//                return null;
//            }
//        }, mContext);
//
//        lRefreshTask.execute();

//
//        String lClientID = clientSecrets.getDetails().getClientId();
//        String lClientSecret = clientSecrets.getDetails().getClientSecret();
//        String lAccessToken = GEUserManager.getInstance(mContext).getmUserInfo().getmAccessToken();
//        String lRefreshToken = GEUserManager.getInstance(mContext).getmUserInfo().getmRefreshToken();
//
//        if (lAuthToken.length() > 0) {
//            {
//                try {
//                    GoogleTokenResponse tokenResponse =
//                            new GoogleAuthorizationCodeTokenRequest(
//                                    new NetHttpTransport(),
//                                    JacksonFactory.getDefaultInstance(),
//                                    "https://www.googleapis.com/oauth2/v4/token",
//                                    lClientID,
//                                    lClientSecret,
//                                    lAuthToken, "")
//                                    .execute();
//                    lAccessToken = tokenResponse.getAccessToken();
//                    lRefreshToken = tokenResponse.getRefreshToken();
//
//                    GEUserManager.getInstance(mContext).setAccessToken(lAccessToken);
//                    GEUserManager.getInstance(mContext).setRefreshToken(lRefreshToken);
//                } catch (IOException e) {
//                    GoogleTokenResponse response = null;
//                    if (lRefreshToken != null && lRefreshToken.length() > 0) {
//                        GoogleRefreshTokenRequest req = new GoogleRefreshTokenRequest(new NetHttpTransport(),
//                                JacksonFactory.getDefaultInstance(), lRefreshToken, lClientID, lClientSecret);
//                        req.setGrantType("refresh_token");
//                        try {
//                            response = req.execute();
//                            System.out.println("RF token = " + response.getAccessToken());
//                            lAccessToken = response.getAccessToken();
//                            GEUserManager.getInstance(mContext).setAccessToken(lAccessToken);
//                        } catch (IOException ex) {
//                            ex.printStackTrace();
//                        }
//                    }
//                    e.printStackTrace();
//                }
//            }
//
//            GoogleCredential lCredential = new GoogleCredential().setAccessToken(lAccessToken);
//            ArrayList scopes = Lists.newArrayList();
//            scopes.add("https://www.googleapis.com/auth/youtube");
//            lCredential .createScoped(scopes);
//            mYTService = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), lCredential ).setApplicationName(
//                    mContext.getString(R.string.app_name)).build();
//        }
//        else
//        {
//
//        }
    }

    private YouTube.Search.List eventQueryFor(GEEventTypes eventTypes, String channelID, String nextPageToken)
    {
        YouTube.Search.List query = null;
        try{
            query = mYTService.search().list("id,snippet");
            query.setKey(GEConstants.GEAPIKEY);
//            query.setOauthToken();
            query.setType("video");
            query.setChannelId(channelID);
            query.setOrder("date");
            query.setMaxResults(50L);
            query.setPageToken(nextPageToken);
            if (eventTypes == GEEventTypes.EFetchEventsLive) {
                query.setEventType("live");
            }
            else if (eventTypes == GEEventTypes.EFetchEventsUpcomming) {
                query.setEventType("upcoming");
            }
            else if (eventTypes == GEEventTypes.EFetchEventsPopularCompleted) {
                query.setEventType("completed");
                query.setOrder("viewCount");
            }
            else if (eventTypes == GEEventTypes.EFetchEventsCompleted)
            {
                query.setEventType("completed");
            }
            else
            {

            }
            query.setFields("items(kind,id/kind,id/videoId,snippet/title,snippet/thumbnails),nextPageToken,pageInfo,prevPageToken");
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }

        return query;
    }

    private YouTube.Videos.List myLikeQueryFor(String nextPageToken)
    {
        if (mYTService == null)
            return null;

        YouTube.Videos.List query = null;
        try{
            query = mYTService.videos().list("id,snippet");
            query.setKey(GEConstants.GEAPIKEY);
            String lAuthToken = GEUserManager.getInstance(mContext).getmUserInfo().getmAuthToken();
            query.setOauthToken(lAuthToken);
            query.setMyRating("like");
            query.setMaxResults(50L);
            query.setPageToken(nextPageToken);
//            query.setFields("items(kind,id/kind,id/videoId,snippet/title,snippet/thumbnails),nextPageToken,pageInfo,prevPageToken");
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }

        return query;
    }

    private YouTube.Videos.List queryForVideos(String lVideoIds, String nextPageToken)
    {
        YouTube.Videos.List query = null;
        try {
            query = mYTService.videos().list("id,snippet,contentDetails,statistics");
            query.setKey(GEConstants.GEAPIKEY);
            query.setMaxResults(50L);
            query.setPageToken(nextPageToken);
            query.setId(lVideoIds);
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }
        return query;
    }

    private String remindersVideoIds()
    {
        String lVideos = "";
        GEReminderDataMgr lReminderMgr = GEReminderDataMgr.getInstance(mContext);
        HashMap<String, HashMap<String, String>> lReminders = lReminderMgr.getmReminders();
        Set<String> lVideoIds = lReminders.keySet();
        for(String lVideoId: lVideoIds){
            lVideos = lVideos + lVideoId + ",";
        }

        if (lVideos.length() > 0)
            lVideos = lVideos.substring(0, lVideos.length() - 1);

        return lVideos;
    }

    private String videoIdsFromResponse(SearchListResponse response)
    {
        String lVideos = "";
        List<SearchResult> lResults = response.getItems();
        int lTotalItem = lResults.size();
        for (int index = 0; index < lTotalItem; ++index)
        {
            SearchResult lResult = lResults.get(index);

            if (index < lTotalItem-1) {
                lVideos = lVideos + lResult.getId().getVideoId() + ",";
            continue;
        }
            lVideos = lVideos + lResult.getId().getVideoId();
        }

        return lVideos;
    }

    private YouTube.Channels.List channelListQueryFor(String channelName, String nextPageToken, boolean lIsId)
    {
        YouTube.Channels.List query = null;
        if (channelName == null)
            return query;
        if (mYTService == null)
            return null;

        try{
            query = mYTService.channels().list("id, snippet, contentDetails, brandingSettings, statistics");
            query.setKey(GEConstants.GEAPIKEY);
            if (lIsId == false)
                query.setForUsername(channelName);
            else
                query.setId(channelName);

            query.setMaxResults(1L);
            query.setPageToken(nextPageToken);
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }

        return query;
    }

    private YouTube.Playlists.List playlistQueryFor(String channelID, String nextPageToken)
    {
        YouTube.Playlists.List query = null;
        try{
            query = mYTService.playlists().list("id,snippet,contentDetails");
            query.setKey(GEConstants.GEAPIKEY);
            query.setChannelId(channelID);
            query.setMaxResults(50L);
            query.setPageToken(nextPageToken);
//            query.setFields("items(kind,id/kind,id/videoId,snippet/title,snippet/thumbnails),nextPageToken,pageInfo,prevPageToken");
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }

        return query;
    }

    private YouTube.PlaylistItems.List playlistItemsQuery(String playlistID, String nextPageToken)
    {
        YouTube.PlaylistItems.List query = null;
        try{
            query = mYTService.playlistItems().list("id,snippet,contentDetails");
            query.setKey(GEConstants.GEAPIKEY);
            query.setMaxResults(50L);
            query.setPlaylistId(playlistID);
            query.setPageToken(nextPageToken);
//            query.setFields("items(kind,id/kind,id/videoId,snippet/title,snippet/thumbnails),nextPageToken,pageInfo,prevPageToken");
        }catch(IOException e){
            Log.d("YC", "Could not initialize: "+e);
        }

        return query;
    }

    private String getChannelIdFromName(String channelName, Boolean lIsId)
    {
        GEChannelManager lManager = GEChannelManager.getInstance();
        Channel lChannel = lManager.channelWithName(channelName);
        if (lChannel != null)
            return lChannel.getId();

        YouTube.Channels.List query = channelListQueryFor(channelName, null, lIsId);
        if (query == null)
            return null;
        if (mYTService == null)
            return null;

        try{
            ChannelListResponse response = query.execute();
            List<Channel> lChannels = response.getItems();
            if (lChannels.size() == 0)
                return channelName;
            lChannel = lChannels.get(0);
            lManager.addChnnelWithName(channelName, lChannel);
            return lChannel.getId();
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }

    public void loadPlaylistAsync(final String channelName, Boolean lIsId)
    {
        final String fChannelName = channelName;
        final Boolean lISId = lIsId;
        mHandler = new android.os.Handler();
        new Thread(){
            public void run(){
                final String lChannelId = getChannelIdFromName(channelName, lISId);

                loadPlaylists(lChannelId, channelName);
                mHandler.post(new Runnable(){
                    public void run(){
                        mListner.playlistsLoadedFromChannel(channelName, true);
                    }
                });
            }
        }.start();
    }

    public void loadPlaylists(String channelID, String ChannelName)
    {
        if (channelID == null)
            return;

        GEPlaylistManager lManager = GEPlaylistManager.getInstance();
        String lNextPageToken = lManager.pageTokenForInfo(ChannelName);
        if (!lManager.canFetchMore(ChannelName) && lNextPageToken == null) {
            return;
        }

        YouTube.Playlists.List query = playlistQueryFor(channelID, lNextPageToken);
        try{
            PlaylistListResponse response = query.execute();
            lManager.addPlaylistListSearchResponse(response, ChannelName);
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
        }
    }

    public void loadPlaylistItemsAsync(String playlistID)
    {
        final String fPlaylistID = playlistID;
        mHandler = new android.os.Handler();
        new Thread(){
            public void run(){
                loadPlaylistItems(fPlaylistID);
                mHandler.post(new Runnable(){
                    public void run(){
                        mListner.playlistsItemsLoadedFromPlaylist(fPlaylistID, true);
                    }
                });
            }
        }.start();
    }

    public void loadPlaylistItems(String playlistID)
    {
        GEVideoListManager lManager = GEVideoListManager.getInstance();
        String lNextPageToken = lManager.pageTokenForInfo(playlistID);
        if (!lManager.canFetchMore(playlistID) && lNextPageToken == null) {
            return;
        }

        YouTube.PlaylistItems.List query = playlistItemsQuery(playlistID, lNextPageToken);
        try{
            PlaylistItemListResponse response = query.execute();
            lManager.addPlaylistItemSearchResponse(response, playlistID);
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
        }
    }

    public void loadEventsAsync(final String channelName, GEEventTypes eventType, boolean isId)
    {
        final  String fChannelId = channelName;
        final  boolean lISId = isId;
        final  GEEventTypes fEventType = eventType;
        mHandler = new android.os.Handler();
        new Thread(){
            public void run(){
                final String lChannelId = getChannelIdFromName(channelName, lISId);

                loadEvents(lChannelId, fEventType, channelName);
                mHandler.post(new Runnable(){
                    public void run(){
                        Log.d("YC", "Could not search: "+fEventType);
                        mListner.eventsLoadedFromChannel(channelName, fEventType, true);
                    }
                });
            }
        }.start();
    }

    public void loadReminders()
    {
        GEEventManager lManager = GEEventManager.getInstance();
        String lNextPageToken = lManager.pageTokenForInfo(GEEventTypes.EFetchEventsReminders, GEConstants.GECHANNELID);
        if (!lManager.canFetchMore(GEEventTypes.EFetchEventsReminders, GEConstants.GECHANNELID) && lNextPageToken == null) {
            return;
        }
    }

    public void loadEvents(String channelID, GEEventTypes eventType, String channelName)
    {
        if (channelID == null)
            return;

        GEEventManager lManager = GEEventManager.getInstance();
        String lNextPageToken = lManager.pageTokenForInfo(eventType, channelID);
        if (!lManager.canFetchMore(eventType, channelID) && lNextPageToken == null){
            return;
        }
        if (eventType == GEEventTypes.EFetchEventsLiked)
        {
            YouTube.Videos.List query = myLikeQueryFor(lNextPageToken);
            try {
                VideoListResponse response = query.execute();
                lManager.addVideoSearchResponse(response, eventType, channelName);
            }catch (UserRecoverableAuthIOException exception){
                mContext.startActivity(exception.getIntent());
            }
            catch (IOException e) {
//                mContext.startActivity(exception.getIntent());
                Log.d("YC", "Could not search: " + e);
            }
        }
        else if (eventType == GEEventTypes.EFetchEventsReminders){
            try {
                String lVideoIds = remindersVideoIds();
                YouTube.Videos.List lVideoQuery = queryForVideos(lVideoIds, lNextPageToken);
                VideoListResponse lVideoResponse = lVideoQuery.execute();
                lManager.addVideoSearchResponse(lVideoResponse, eventType, channelName);
            } catch (IOException e) {
                Log.d("YC", "Could not search: " + e);
            }
        }
        else
        {
            YouTube.Search.List query = eventQueryFor(eventType, channelID, lNextPageToken);
            try {
                SearchListResponse response = query.execute();
                String lVideoIds = videoIdsFromResponse(response);
                YouTube.Videos.List lVideoQuery = queryForVideos(lVideoIds, null);
                VideoListResponse lVideoResponse = lVideoQuery.execute();
                if (response.getItems().size() >= 50) {//Sometimes results are less than page size and next page token exist in response
                    lVideoResponse.setNextPageToken(response.getNextPageToken());
                    lVideoResponse.setPrevPageToken(response.getPrevPageToken());
                }
                lManager.addVideoSearchResponse(lVideoResponse, eventType, channelName);
            } catch (IOException e) {
                Log.d("YC", "Could not search: " + e);
            }
        }
    }
}
