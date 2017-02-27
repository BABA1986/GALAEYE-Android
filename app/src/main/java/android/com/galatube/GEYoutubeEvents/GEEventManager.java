package android.com.galatube.GEYoutubeEvents;

import com.google.api.services.youtube.model.SearchListResponse;

import java.util.ArrayList;
import java.util.List;

/*****
 * Created by deepak on 04/01/17.
 *****/

public class GEEventManager {
    private static GEEventManager ourInstance = new GEEventManager();

    public static GEEventManager getInstance() {
        return ourInstance;
    }

    private ArrayList<GEEventListObj> mEventListObjs;

    private GEEventManager() {
        mEventListObjs = new ArrayList<GEEventListObj>() {
        };
    }

    public ArrayList<GEEventListObj> getmEventListObjs() {
        return mEventListObjs;
    }

    public String pageTokenForInfo(GEEventTypes eventType, String channelID)
    {
        GEEventListObj lGEEventListObj = eventListObjForInfo(eventType, channelID);
        if (lGEEventListObj == null)
        {
            return null;
        }

        GEEventListPage lLastPage = lGEEventListObj.getmEventListPages().get(lGEEventListObj.getmEventListPages().size()-1) ;
        return lLastPage.getmNextPageToken();
    }

    public boolean canFetchMore(GEEventTypes eventType, String channelID)
    {
        GEEventListObj lGEEventListObj = eventListObjForInfo(eventType, channelID);
        if (lGEEventListObj == null)
        {
            return true;
        }

        GEEventListPage lLastPage = lGEEventListObj.getmEventListPages().get(lGEEventListObj.getmEventListPages().size()-1) ;

        if (lLastPage.getmNextPageToken() == null){
            return false;
        }

        if (lLastPage.getmNextPageToken().length() > 0) {
            return true;
        }

        return false;
    }

    public void addEventSearchResponse(SearchListResponse response, GEEventTypes eventType, String channelID)
    {
        GEEventListObj lGEEventListObj = null;
        if (response.getItems().size() == 0) {
            return;
        }

        lGEEventListObj = eventListObjForInfo(eventType, channelID);

        if(lGEEventListObj == null)
        {
            lGEEventListObj = new GEEventListObj(response, eventType, channelID);
            mEventListObjs.add(lGEEventListObj);
        }
        else
        {
            lGEEventListObj.addEventFromResponse(response);
        }
    }

    public GEEventListObj eventListObjForInfo(GEEventTypes eventType, String channelID)
    {
        GEEventListObj lListObj = null;
        GEEventListObj lRetListObj = null;
        for (int index = 0; index < mEventListObjs.size(); ++index)
        {
            lListObj = mEventListObjs.get(index);
            if ((eventType == lListObj.getmEventType()) && (lListObj.getmChannelSource().equalsIgnoreCase(channelID)))
            {
                lRetListObj = lListObj;
                break;
            }
        }

        return lRetListObj;
    }
}