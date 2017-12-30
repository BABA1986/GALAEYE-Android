package gala.com.urtube.GELaunchModal;

import android.app.Activity;
import android.net.Uri;
import android.support.annotation.NonNull;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.dynamiclinks.FirebaseDynamicLinks;
import com.google.firebase.dynamiclinks.PendingDynamicLinkData;

/**
 * Created by deepak on 25/12/17.
 */

public class GELaunchLinkInfo
{
    public String        mChannelSrc;
    public String        mVideoId;
    public boolean       mIsChannelId;

    private GELaunchLinkResolveListner      mListner;


    public GELaunchLinkInfo(Activity mainActivity, GELaunchLinkResolveListner listner) {
        mListner = listner;
        initialseDynamicLinkIfAny(mainActivity);
    }

    private void initialseDynamicLinkIfAny(final Activity launchActivity){
        FirebaseDynamicLinks.getInstance()
                .getDynamicLink(launchActivity.getIntent())
                .addOnSuccessListener(launchActivity, new OnSuccessListener<PendingDynamicLinkData>() {
                    @Override
                    public void onSuccess(PendingDynamicLinkData pendingDynamicLinkData) {
                        // Get deep link from result (may be null if no link is found)
                        Uri deepLink = null;
                        if (pendingDynamicLinkData != null) {
                            deepLink = pendingDynamicLinkData.getLink();

                            mChannelSrc = deepLink.getQueryParameter("channelsrc");
                            mVideoId = deepLink.getQueryParameter("videoid");
                            mIsChannelId = Boolean.parseBoolean(deepLink.getQueryParameter("ischannelid"));

                            mListner.onResolveLaunchLink(GELaunchLinkInfo.this, true);
//                            Toast.makeText(launchActivity,
//                                    deepLink.toString(), Toast.LENGTH_LONG).show();
                        }

                    }
                })
                .addOnFailureListener(launchActivity, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
//                        Toast.makeText(launchActivity,
//                                "failed linking", Toast.LENGTH_LONG).show();
                        mListner.onResolveLaunchLink(GELaunchLinkInfo.this, false);
                    }
                });
    }
}
