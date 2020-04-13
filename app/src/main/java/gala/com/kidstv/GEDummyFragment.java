package gala.com.kidstv;


import android.os.Bundle;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

/**
 * Created by deepak on 18/01/17.
 */

public class GEDummyFragment extends Fragment
{
    public static final String ARG_PAGE = "ARG_PAGE";

    // Your developer key goes here
    public static GEDummyFragment newInstance(int page) {
        Bundle args = new Bundle();
        args.putInt(ARG_PAGE, page);
        GEDummyFragment lGEDummyFragment = new GEDummyFragment();
        lGEDummyFragment.setArguments(args);
        return lGEDummyFragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dummy_fragment, container, false);
        return view;
    }
}
