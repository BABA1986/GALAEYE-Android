package android.com.galatube;

import android.com.galatube.GEPlaylist.GEPlaylistFragment;
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

    public GEPageAdapter(FragmentManager manager, Context context) {
        super(manager);
        this.mContext = context;
    }

    public GEPageAdapter(FragmentManager manager, Context context, ArrayList<GESubMenu> subMenus) {
        super(manager);

        this.mContext = context;
        this.mSubMenus = subMenus;
    }

    public void reloadWithSubMenu(ArrayList<GESubMenu> subMenus)
    {
        this.mSubMenus = subMenus;
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
            return GEVideoListFragment.newInstance(position + 1);
        }
        if (lSubMenu.getmSubMenuType().equals("playlist") == true)
        {
            Bundle lArguments = new Bundle();
            lArguments.putString("channelName", lSubMenu.getmSubMenuSrc());
            GEPlaylistFragment lGEPlaylistFragment = GEPlaylistFragment.newInstance(position + 1);
            lGEPlaylistFragment.setArguments(lArguments);
            return lGEPlaylistFragment;
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
