package android.com.galatube;

import android.com.galatube.GETheme.GEThemeManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import static android.content.Context.MODE_PRIVATE;

/**
 * Created by admin on 15/04/17.
 */
public class GEPlayeventAdapter extends RecyclerView.Adapter<GEPlayeventAdapter.MyViewHolder> {
    Context mContext;

    public class MyViewHolder extends RecyclerView.ViewHolder  {
        private final TextView mPlaylistmenu;

        public MyViewHolder(View itemView) {
            super(itemView);
            mPlaylistmenu = (TextView) itemView.findViewById(R.id.textViewOptions);
        }
    }

    public GEPlayeventAdapter(Context context) {
        this.mContext=context;

    }

    @Override
    public GEPlayeventAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.playevent, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final GEPlayeventAdapter.MyViewHolder holder, int position) {
        SharedPreferences sharedPreferences=mContext.getSharedPreferences("myTheme",MODE_PRIVATE);
        GEThemeManager.getInstance(mContext).setmSelectedIndex(sharedPreferences.getInt("MyThemePosition",0));
        int lColor = GEThemeManager.getInstance(mContext).getSelectedNavColor();
        holder.mPlaylistmenu.setTextColor(lColor);
        holder.mPlaylistmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                PopupMenu popup = new PopupMenu(mContext, holder.mPlaylistmenu);
                //inflating menu from xml resource
                popup.inflate(R.menu.play_list_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.menu1:
                                //handle menu1 click
                                break;
                            case R.id.menu2:
                                //handle menu2 click
                                break;
                            case R.id.menu3:
                                //handle menu3 click
                                break;
                        }
                        return false;
                    }
                });
                //displaying the popup
                popup.show();

            }
        });

    }

    @Override
    public int getItemCount() {
        return 15;
    }


}
