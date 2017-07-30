package android.com.galatube.GEYoutubeEvents;

/**
 * Created by deepak on 30/12/16.
 */

import android.accounts.Account;
import android.com.galatube.*;
import android.com.galatube.GEConstants;
import android.com.galatube.GEPlaylist.GEPlaylistManager;
import android.com.galatube.GEPlaylist.GEVideoListManager;
import android.com.galatube.GEUserModal.GEAuth;
import android.com.galatube.GEUserModal.GEUserManager;
import android.content.Context;
import android.nfc.Tag;
import android.os.StrictMode;
import android.util.Log;

import com.google.android.gms.auth.GoogleAuthException;
import com.google.android.gms.auth.GoogleAuthUtil;
import com.google.android.gms.auth.UserRecoverableAuthException;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.credentials.Credential;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.api.client.auth.oauth2.AuthorizationCodeFlow;
import com.google.api.client.auth.oauth2.BearerToken;
import com.google.api.client.auth.oauth2.StoredCredential;
import com.google.api.client.auth.oauth2.TokenRequest;
import com.google.api.client.auth.oauth2.TokenResponse;
import com.google.api.client.extensions.java6.auth.oauth2.AuthorizationCodeInstalledApp;
import com.google.api.client.extensions.jetty.auth.oauth2.LocalServerReceiver;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleCredential;
import com.google.api.client.googleapis.auth.oauth2.GoogleOAuthConstants;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.googleapis.json.GoogleJsonResponseException;
import com.google.api.client.googleapis.services.GoogleClientRequestInitializer;
import com.google.api.client.http.HttpRequest;
import com.google.api.client.http.HttpRequestInitializer;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.gson.GsonFactory;
import com.google.api.client.json.jackson2.JacksonFactory;
import com.google.api.client.util.Lists;
import com.google.api.client.util.store.MemoryDataStoreFactory;
import com.google.api.services.youtube.YouTube;
import com.google.api.services.youtube.YouTubeScopes;
import com.google.api.services.youtube.model.Channel;
import com.google.api.services.youtube.model.ChannelListResponse;
import com.google.api.services.youtube.model.PlaylistItemListResponse;
import com.google.api.services.youtube.model.PlaylistListResponse;
import com.google.api.services.youtube.model.SearchListResponse;
import com.google.api.services.youtube.model.SearchResult;
import com.google.api.services.youtube.model.VideoListResponse;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Logger;

import static com.google.api.services.youtube.YouTubeScopes.*;

public class GEServiceManager
{
    private Context             mContext;
    private YouTube             mYTService;
    private GEEventListner      mListner;;
    private android.os.Handler  mHandler;

    public GEServiceManager(GEEventListner listner, Context context) throws IOException {
        this.mContext = context;
        this.mListner = listner;

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
        StrictMode.setThreadPolicy(policy);

        Reader clientSecretReader = new InputStreamReader(Auth.class.getResourceAsStream("/client_secrets.json"));
        GoogleClientSecrets clientSecrets = GoogleClientSecrets.load(new JacksonFactory(), clientSecretReader);

        String lAuthToken = GEUserManager.getInstance(mContext).getmUserInfo().getmAuthToken();
        String lClientID = clientSecrets.getDetails().getClientId();
        String lClientSecret = clientSecrets.getDetails().getClientSecret();
        String lAccessToken = GEUserManager.getInstance(mContext).getmUserInfo().getmAccessToken();
        String lRefreshToken = GEUserManager.getInstance(mContext).getmUserInfo().getmRefreshToken();

        if (lAuthToken.length() > 0) {
            {
                try {
                    GoogleTokenResponse tokenResponse =
                            new GoogleAuthorizationCodeTokenRequest(
                                    new NetHttpTransport(),
                                    JacksonFactory.getDefaultInstance(),
                                    "https://www.googleapis.com/oauth2/v4/token",
                                    lClientID,
                                    lClientSecret,
                                    lAuthToken, "")  // Specify the same redirect URI that you use with your web
                                    // app. If you don't have a web version of your app, you can
                                    // specify an empty string.
                                //.setGrantType("refresh_token")


                                    .execute();
                    lAccessToken = tokenResponse.getAccessToken();
                    lRefreshToken = tokenResponse.getRefreshToken();

                    GEUserManager.getInstance(mContext).setAccessToken(lAccessToken);
                    GEUserManager.getInstance(mContext).setRefreshToken(lRefreshToken);
                } catch (IOException e) {
                    GoogleTokenResponse response = null;
                    if (lRefreshToken != null && lRefreshToken.length() > 0) {
                        GoogleRefreshTokenRequest req = new GoogleRefreshTokenRequest(new NetHttpTransport(),
                                JacksonFactory.getDefaultInstance(), lRefreshToken, lClientID, lClientSecret);
                        req.setGrantType("refresh_token");
                        try {
                            response = req.execute();
                            System.out.println("RF token = " + response.getAccessToken());
                            lAccessToken = response.getAccessToken();
                            GEUserManager.getInstance(mContext).setAccessToken(lAccessToken);
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }
                    e.printStackTrace();
                }
            }

            GoogleCredential lCredential = new GoogleCredential().setAccessToken(lAccessToken);
            if (lRefreshToken != null && lRefreshToken.length() > 0) {
//            lCredential.setRefreshToken(lRefreshToken);
            }

            ArrayList scopes = Lists.newArrayList();
            scopes.add("https://www.googleapis.com/auth/youtube");
            lCredential .createScoped(scopes);
            try {
                boolean lRefreshed = lCredential.refreshToken();
                if (lRefreshed == true)
                    Log.d("YC", "refreshed ");
                else
                    Log.d("YC", "Not refreshed ");
            }
            catch (IOException e) {
                e.printStackTrace();
            }
            mYTService = new YouTube.Builder(new NetHttpTransport(), new GsonFactory(), lCredential ).setApplicationName(
                    mContext.getString(R.string.app_name)).build();
        }
        else
        {
            mYTService = new YouTube.Builder(new NetHttpTransport(),
                    new JacksonFactory(), new HttpRequestInitializer() {
                @Override
                public void initialize(HttpRequest hr) throws IOException {

                }
            }).setApplicationName(mContext.getString(R.string.app_name)).build();
        }
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

    private YouTube.Channels.List channelListQueryFor(String channelName, String nextPageToken)
    {
        YouTube.Channels.List query = null;
        try{
            query = mYTService.channels().list("id");
            query.setKey(GEConstants.GEAPIKEY);
            query.setForUsername(channelName);
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

    private String getChannelIdFromName(String channelName)
    {
        YouTube.Channels.List query = channelListQueryFor(channelName, null);
        try{
            ChannelListResponse response = query.execute();
            List<Channel> lChannels = response.getItems();
            if (lChannels.size() == 0)
                return channelName;
            Channel lChannel = lChannels.get(0);
            return lChannel.getId();
        }catch(IOException e){
            Log.d("YC", "Could not search: "+e);
            return null;
        }
    }

    public void loadPlaylistAsync(final String channelName)
    {
        final String fChannelName = channelName;
        mHandler = new android.os.Handler();
        new Thread(){
            public void run(){
                final String lChannelId = getChannelIdFromName(channelName);
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

    public void loadEventsAsync(final String channelName, GEEventTypes eventType)
    {
        final  String fChannelId = channelName;
        final  GEEventTypes fEventType = eventType;
        mHandler = new android.os.Handler();
        new Thread(){
            public void run(){
                final String lChannelId = getChannelIdFromName(channelName);
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

    public void loadEvents(String channelID, GEEventTypes eventType, String channelName)
    {
        GEEventManager lManager = GEEventManager.getInstance();
        String lNextPageToken = lManager.pageTokenForInfo(eventType, channelID);
        if (!lManager.canFetchMore(eventType, channelID) && lNextPageToken == null)
        {
            return;
        }
        if (eventType == GEEventTypes.EFetchEventsLiked)
        {
            YouTube.Videos.List query = myLikeQueryFor(lNextPageToken);
            try {
                VideoListResponse response = query.execute();
                lManager.addMyLikedSearchResponse(response, eventType, channelName);
            } catch (IOException e) {
                Log.d("YC", "Could not search: " + e);
            }
        }
        else
        {
            YouTube.Search.List query = eventQueryFor(eventType, channelID, lNextPageToken);
            try {
                SearchListResponse response = query.execute();
                lManager.addEventSearchResponse(response, eventType, channelName);
            } catch (IOException e) {
                Log.d("YC", "Could not search: " + e);
            }
        }
    }
}
