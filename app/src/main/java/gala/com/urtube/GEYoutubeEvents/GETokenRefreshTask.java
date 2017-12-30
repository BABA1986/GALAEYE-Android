package gala.com.urtube.GEYoutubeEvents;

import gala.com.urtube.GEUserModal.GEUserManager;
import android.content.Context;
import android.os.AsyncTask;

import com.google.android.gms.auth.api.Auth;
import com.google.api.client.auth.oauth2.AuthorizationCodeRequestUrl;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeFlow;
import com.google.api.client.googleapis.auth.oauth2.GoogleAuthorizationCodeTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleClientSecrets;
import com.google.api.client.googleapis.auth.oauth2.GoogleRefreshTokenRequest;
import com.google.api.client.googleapis.auth.oauth2.GoogleTokenResponse;
import com.google.api.client.http.javanet.NetHttpTransport;
import com.google.api.client.json.jackson2.JacksonFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.Collection;

/**
 * Created by deepak on 23/09/17.
 */

public class GETokenRefreshTask extends AsyncTask<Void, Void, String>
{
    public GETokenRefreshListner    mListner= null;
    private Context                 mContext;

    public GETokenRefreshTask(GETokenRefreshListner asyncResponse, Context context) {
        mListner = asyncResponse;//Assigning call back interfacethrough constructor
        mContext = context;
    }

    @Override
    protected String doInBackground(Void... params) {
        Reader clientSecretReader = new InputStreamReader(Auth.class.getResourceAsStream("/client_secrets.json"));
        GoogleClientSecrets clientSecrets = null;
        try {
            clientSecrets = GoogleClientSecrets.load(new JacksonFactory(), clientSecretReader);
        } catch (IOException e) {
            e.printStackTrace();
            return "error";
        }

        String lAuthToken = GEUserManager.getInstance(mContext).getmUserInfo().getmAuthToken();
        String lClientID = clientSecrets.getDetails().getClientId();
        String lClientSecret = clientSecrets.getDetails().getClientSecret();
        String lAccessToken = GEUserManager.getInstance(mContext).getmUserInfo().getmAccessToken();
        String lRefreshToken = GEUserManager.getInstance(mContext).getmUserInfo().getmRefreshToken();

        if (lAuthToken.length() == 0) {
            return "error";
        }
        try {
//            requestUrl = new GoogleAuthorizationCodeRequestUrl(lClientID, "https://www.googleapis.com/oauth2/v4/token", scopes).setAccessType("offline").build();

            ArrayList<String> lScopeList = new ArrayList<String>();
            lScopeList.add("");
            Collection<String> lScopes = new ArrayList<String>(lScopeList);

            GoogleAuthorizationCodeFlow flow = new GoogleAuthorizationCodeFlow.Builder(
                    new NetHttpTransport(),
                    JacksonFactory.getDefaultInstance(), clientSecrets, lScopes)
                    .build();

            AuthorizationCodeRequestUrl authorizationUrl =
                    flow.newAuthorizationUrl().setRedirectUri("https://www.googleapis.com/oauth2/v4/token")
                            .setApprovalPrompt("force")
                            .setAccessType("offline");
            authorizationUrl.build();



            GoogleTokenResponse tokenResponse =
                    new GoogleAuthorizationCodeTokenRequest(
                            new NetHttpTransport(),
                            JacksonFactory.getDefaultInstance(),
                            "https://www.googleapis.com/oauth2/v4/token",
                            lClientID,
                            lClientSecret,
                            lAuthToken, "")
                            .execute();
            lAccessToken = tokenResponse.getAccessToken();
            GEUserManager.getInstance(mContext).setAccessToken(lAccessToken);

            if (lRefreshToken == null || lRefreshToken.length() == 0) {
                lRefreshToken = tokenResponse.getRefreshToken();
                GEUserManager.getInstance(mContext).setRefreshToken(lRefreshToken);
            }
            return lAccessToken;
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
                    return lAccessToken;
                } catch (IOException ex) {
                    ex.printStackTrace();
                    return "error";
                }
            }
            e.printStackTrace();
            return "error";
        }
    }

    @Override
    protected void onPostExecute(String result) {
        mListner.onCompleteRefreshToken(result);
    }
}
