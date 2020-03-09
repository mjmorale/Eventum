package ch.epfl.sdp.ui.main;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.android.material.tabs.TabLayout;

import java.util.Date;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.epfl.sdp.DatabaseEventBuilder;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.ui.event.EventFragment;
import ch.epfl.sdp.ui.swipe.SwipeFragment;

public class MainFragment extends Fragment implements TabLayout.BaseOnTabSelectedListener {

    private MainViewModel mViewModel;

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    private SwipeFragment mSwipeFragment;
    private AuthFragment mAuthFragment;
    private EventFragment mEventFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        try {
            DatabaseObjectBuilderFactory.registerBuilder(Event.class, DatabaseEventBuilder.class);
        } catch (Exception e) {
            Log.e("ObjectBuilderFactory", "", e);
        }

        mSwipeFragment = new SwipeFragment();
        mAuthFragment = new AuthFragment();
        mEventFragment = new EventFragment();
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
