package gala.com.kidstv.GEYoutubeEvents;

import gala.com.kidstv.GEConstants;
import android.content.Context;

import java.util.HashMap;

/**
 * Created by deepak on 01/10/17.
 */

public class GEReminderDataMgr
{
    private GEReminderDB                             mReminderDB;
    private Context                                  mContext;
    private static GEReminderDataMgr                 sInstance;
    private HashMap<String, HashMap<String, String>> mReminders;

    public static GEReminderDataMgr getInstance(Context context) {
        if (sInstance == null)
            sInstance = new GEReminderDataMgr(context);

        return sInstance;
    }

    private GEReminderDataMgr(Context context)
    {
        mContext = context;
        mReminderDB = new GEReminderDB(context);
        mReminders = mReminderDB.getAllReminders();
    }

    public void addReminderWinInfo(String videoId, String dateTime)
    {
        mReminderDB.addReminder(videoId, dateTime);
        HashMap<String, String> lVideoInfo = new HashMap<String, String>();
        lVideoInfo.put(GEConstants.KEY_VIDEO_ID, videoId);
        lVideoInfo.put(GEConstants.KEY_DATETIME, dateTime);
        mReminders.put(videoId, lVideoInfo);
    }

    public void deleteReminderWinInfo(String videoId)
    {
        mReminderDB.deleteReminder(videoId);
        mReminders.remove(videoId);
    }

    public boolean isVideoReminderWithId(String videoId)
    {
        HashMap<String, String> lVideoInfo = mReminders.get(videoId);
        return (lVideoInfo != null);
    }

    public HashMap<String, HashMap<String, String>> getmReminders()
    {
        return mReminders;
    }
}
