package gala.com.kidstv.GEYoutubeEvents;

import androidx.recyclerview.widget.RecyclerView;
import android.view.View;

/**
 * Created by deepak on 23/02/17.
 */

public interface GERecyclerItemClickListner
{
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position);
    public void onRecyclerItemClicked(View view, RecyclerView.ViewHolder viewHolder, int position, GEEventTypes eventType);
}
