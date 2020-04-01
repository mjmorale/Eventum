package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;
import android.widget.ArrayAdapter;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.firebase.firestore.FirebaseFirestore;
import com.lorentzos.flingswipe.SwipeFlingAdapterView;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentSwipeBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.ui.main.attending.AttendingEventAdapter;
import ch.epfl.sdp.ui.main.attending.AttendingListViewModel;

public class SwipeFragment2 extends Fragment {

    //-----------------
    private final EventSwipeViewModel2.EventSwipeViewModelFactory2 mFactory;
    private FragmentSwipeBinding mBinding;
    private CardArrayAdapter2 mAdapter;
    private EventSwipeViewModel2 mViewModel;
    //-----------------



    private ArrayAdapter<Event> mArrayAdapter;
    private EventDetailFragment mInfoFragment;
    private List<Event> mEventList;
    private Event mCurrentEvent;

    public SwipeFragment2() {
        mFactory = new EventSwipeViewModel2.EventSwipeViewModelFactory2();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    @VisibleForTesting
    public SwipeFragment2(@NonNull Database database) {
        mFactory = new EventSwipeViewModel2.EventSwipeViewModelFactory2();
        mFactory.setDatabase(database);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mBinding = FragmentSwipeBinding.inflate(inflater, container, false);
        return mBinding.getRoot();
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mEventList = new ArrayList<>();
        mArrayAdapter = new CardArrayAdapter(getContext(), mEventList);
        mBinding.cardsListView.setAdapter( mArrayAdapter);
        mBinding.cardsListView.setFlingListener(new SwipeFlingAdapterView.onFlingListener() {
            @Override
            public void removeFirstObjectInAdapter() {
                mEventList.remove(0);
                mArrayAdapter.notifyDataSetChanged();
                mCurrentEvent = mEventList.get(0);
            }

            @Override
            public void onLeftCardExit(Object o) {}

            @Override
            public void onRightCardExit(Object o) {}

            @Override
            public void onAdapterAboutToEmpty(int i) {}

            @Override
            public void onScroll(float v) {}
        });


        mViewModel = new ViewModelProvider(this, mFactory).get(EventSwipeViewModel2.class);
        if(mViewModel.getAttendingEvents().hasObservers()) {
            mViewModel.getAttendingEvents().removeObservers(getViewLifecycleOwner());
        }
        mViewModel.getAttendingEvents().observe(getViewLifecycleOwner(), events -> {
            mArrayAdapter.addAll(events);
        });
        
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }
}
