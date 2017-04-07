package android.com.galatube;

import android.com.galatube.GEPlaylist.GEPlaylistFragment;
import android.com.galatube.GEYoutubeEvents.GEEventTypes;
import android.com.galatube.model.GEMenu.GEMenu;
import android.com.galatube.model.GEMenu.GESubMenu;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

/**
 * Created by deepak on 10/12/16.
 */

public class GEPageAdapter extends FragmentStatePagerAdapter {
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
        if (lSubMenu.getmSubMenuName().equals("Popular") == true)
        {
            return GEVideoListFragment.newInstance(position + 1, GEEventTypes.EFetchEventsPopularCompleted, GEConstants.GECHANNELID);
        }
        if (lSubMenu.getmSubMenuType().equals("playlist") == true)
        {
            if (mContentFilterType.equals("playlists") == true) {
                Bundle lArguments = new Bundle();
                lArguments.putString("channelName", lSubMenu.getmSubMenuSrc());
                GEPlaylistFragment lGEPlaylistFragment = GEPlaylistFragment.newInstance(position + 1);
                lGEPlaylistFragment.setArguments(lArguments);
                return lGEPlaylistFragment;
            }
            else{
                return GEVideoListFragment.newInstance(position + 1, GEEventTypes.EFetchEventsArchivedVideos, lSubMenu.getmSubMenuSrc());
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
