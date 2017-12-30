package gala.com.urtube;

import gala.com.urtube.GEPlaylist.GEPlaylistFragment;
import gala.com.urtube.GEYoutubeEvents.GEEventTypes;
import gala.com.urtube.GEYoutubeEvents.GELikedListFragment;
import gala.com.urtube.GEYoutubeEvents.GEReminderListFragment;
import gala.com.urtube.model.GEMenu.GESubMenu;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by deepak on 10/12/16.
 */

public class GEPageAdapter extends FragmentStatePagerAdapter
{
    private ArrayList<GESubMenu>           mSubMenus;
    private Context                        mContext;
    private String                         mContentFilterType;

    public GEPageAdapter(FragmentManager manager, Context context, String contentFilterType)
    {
        super(manager);
        this.mContext = context;
        this.mContentFilterType = contentFilterType;
    }

    public GEPageAdapter(FragmentManager manager,
                         Context context,
                         ArrayList<GESubMenu> subMenus,
                         String contentFilterType)
    {
        super(manager);

        this.mContext = context;
        this.mSubMenus = subMenus;
        this.mContentFilterType = contentFilterType;
    }

    public void reloadWithSubMenu(ArrayList<GESubMenu> subMenus, String filter)
    {
        this.mSubMenus = subMenus;
        this.mContentFilterType = filter;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return mSubMenus.size();
    }

    @Override
    public Fragment getItem(int position)
    {
        GESubMenu lSubMenu = mSubMenus.get(position);

        if (lSubMenu.getmSubMenuName().equals("Reminders") == true)
        {
            return GEReminderListFragment.newInstance(position + 1, GEConstants.GECHANNELID);
        }
        if (lSubMenu.getmSubMenuName().equals("Popular") == true)
        {
            return GEPopularEventListFragment.newInstance(position + 1, GEEventTypes.EFetchEventsPopularCompleted, GEConstants.GECHANNELID, true);
        }
        if (lSubMenu.getmSubMenuName().equals("My Liked") == true)
        {
            return GELikedListFragment.newInstance(position + 1, GEConstants.GECHANNELID);
        }
        if (lSubMenu.getmSubMenuType().equals("video") == true)
        {
            if (mContentFilterType.equals("playlists") == true) {
                Bundle lArguments = new Bundle();
                lArguments.putString("channelName", lSubMenu.getmSubMenuSrc());
                lArguments.putBoolean("ischannelId", lSubMenu.ismIsChannelId());
                GEPlaylistFragment lGEPlaylistFragment = GEPlaylistFragment.newInstance(position + 1);
                lGEPlaylistFragment.setArguments(lArguments);
                return lGEPlaylistFragment;
            }
            else{
                return GEPopularEventListFragment.newInstance(position + 1, GEEventTypes.EFetchEventsArchivedVideos, lSubMenu.getmSubMenuSrc(), lSubMenu.ismIsChannelId());
            }
        }
        else if (position > 0) {
            return GEDummyFragment.newInstance(position + 1);
        }

//        playlist
        return GEEventListFragment.newInstance(position + 1);
    }

    @Override
    public int getItemPosition(Object object) {
        // Causes adapter to reload all Fragments when
        // notifyDataSetChanged is called
        return POSITION_NONE;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        // Generate title based on item position
        GESubMenu lSubMenu = mSubMenus.get(position);
        return lSubMenu.getmSubMenuName();
    }

}
