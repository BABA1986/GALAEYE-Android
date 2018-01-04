package gala.com.urtube.GEPlayer;

import android.content.Context;

import com.google.android.gms.ads.AdListener;
import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.InterstitialAd;

/**
 * Created by deepak on 09/12/17.
 */

public class GEInterstitialAdMgr {

    private int                                 mAttemptCount;
    private Context                             mContext;
    private InterstitialAd                      mInterstitialAd;
    private static GEInterstitialAdMgr ourInstance = new GEInterstitialAdMgr();

    public static GEInterstitialAdMgr initWithContext(Context context) {
        ourInstance.mContext = context;
        ourInstance.initInterstitialAd();
        return ourInstance;
    }

    public static void showInterstitialAd() {
        if (ourInstance.mAttemptCount % 5 == 0) {
            ourInstance.mInterstitialAd.loadAd(new AdRequest.Builder().build());
        }

        ourInstance.mAttemptCount += 1;
    }

    private void initInterstitialAd(){
        mInterstitialAd = new InterstitialAd(mContext);
        mAttemptCount = 1;
        mInterstitialAd.setAdUnitId("ca-app-pub-5685624800532639/9624409126");
        mInterstitialAd.setAdListener(new AdListener() {
            @Override
            public void onAdLoaded() {
                // Code to be executed when an ad finishes loading.
                mInterstitialAd.show();
                ourInstance.resetAttempts();
            }

            @Override
            public void onAdFailedToLoad(int errorCode) {
                // Code to be executed when an ad request fails.
//                mPlayer.play();
            }

            @Override
            public void onAdOpened() {
                // Code to be executed when the ad is displayed.
//                mPlayer.pause();
            }

            @Override
            public void onAdLeftApplication() {
                // Code to be executed when the user has left the app.
            }

            @Override
            public void onAdClosed() {
                // Code to be executed when when the interstitial ad is closed.
            }
        });
    }

    public void resetAttempts(){
        mAttemptCount = 1;
    }

}
