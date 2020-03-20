package ch.epfl.sdp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import java.util.Date;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.eventdetail.EventDetailFragment;
import ch.epfl.sdp.ui.swipe.SwipeFragment;

public class MainFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private SwipeFragment mSwipeFragment;
    private AuthFragment mAuthFragment;
    private EventDetailFragment mEventFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mSwipeFragment = new SwipeFragment();
        mAuthFragment = new AuthFragment();
        mEventFragment = new EventDetailFragment(new Event("OSS-117 Movie watching",
                "We will watch OSS-117: Cairo, Nest of Spies and then we can exchange about why this is the best movie of all times",
                new Date(2021, 1, 16), R.drawable.oss_117), null);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.main_fragment, container, false);
        TabLayout layout = view.findViewById(R.id.tabLayout);
        layout.addOnTabSelectedListener(this);
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, mSwipeFragment)
                .commit();

        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mViewModel = ViewModelProviders.of(this).get(MainViewModel.class);
    }

    @Override
    public void onTabSelected(TabLayout.Tab tab) {
        Fragment toInsert = null;
        switch(tab.getPosition()) {
            case 0:
                toInsert = mSwipeFragment;
                break;
            case 1:
                toInsert = mAuthFragment;
                break;
            case 2:
                toInsert = mEventFragment;
                break;
        }
        getActivity().getSupportFragmentManager().beginTransaction()
                .replace(R.id.fragment_container, toInsert)
                .commitNow();
    }

    @Override
    public void onTabUnselected(TabLayout.Tab tab) {

    }

    @Override
    public void onTabReselected(TabLayout.Tab tab) {

    }
}
