package gala.com.kidstv.Refactored.items.MediaInfo;

import android.view.View;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.github.vivchar.rendererrecyclerviewadapter.ViewHolder;

import gala.com.kidstv.R;

public class MediaViewHolder extends ViewHolder {

    public final TextView               mName;
    public final TextView               mMediaDesc;
    public final TextView               mMediaTypeStr;

    public MediaViewHolder(final View itemView) {
        super(itemView);
        mName = (TextView) itemView.findViewById(R.id.title);
        mMediaDesc = (TextView) itemView.findViewById(R.id.mediatype);
        mMediaTypeStr = itemView.findViewById(R.id.mediatypestr);
    }

}