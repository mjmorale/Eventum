package ch.epfl.sdp.ui.main;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.material.tabs.TabLayout;
import com.google.firebase.firestore.FirebaseFirestore;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import ch.epfl.sdp.R;

import ch.epfl.sdp.ui.map.MapFragment;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.event.CreateEventFragment;
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
    private MapFragment mMapFragment;
    private CreateEventFragment mCreateEventFragment;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Database db = new FirestoreDatabase(FirebaseFirestore.getInstance());
        mSwipeFragment = new SwipeFragment();
        mAuthFragment = new AuthFragment();
        mMapFragment = new MapFragment();
        mEventFragment = EventFragment.newInstance("fake", db);
        mCreateEventFragment = new CreateEventFragment(db);
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
            case 3:
                toInsert = mMapFragment;
               break;
            case 4:
                toInsert = mCreateEventFragment;
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
