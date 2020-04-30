package ch.epfl.sdp.ui.event;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.ViewModelProvider;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.provider.CalendarContract;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.MapView;
import com.google.firebase.firestore.FirebaseFirestore;
import java.util.GregorianCalendar;

import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentDefaultEventBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.chat.ChatFragment;
import ch.epfl.sdp.ui.sharing.Sharing;
import ch.epfl.sdp.ui.sharing.SharingBuilder;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventFragment extends Fragment{

    private DefaultEventViewModel mViewModel;
    private FragmentDefaultEventBinding mBinding;
    private final DefaultEventViewModel.DefaultEventViewModelFactory mFactory;
    private Sharing mEventSharing;
    private MapView mMapView;
    private float mZoomLevel = 15;
    private int LAUNCH_CALENDAR = 3;
    public static DefaultEventFragment getInstance(@NonNull String eventRef) {
        verifyNotNull(eventRef);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, eventRef);

        DefaultEventFragment fragment = new DefaultEventFragment();
        fragment.setArguments(bundle);
        return fragment;
    }

    public DefaultEventFragment() {
        mFactory = new DefaultEventViewModel.DefaultEventViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));

    }

    @VisibleForTesting
    public DefaultEventFragment(@NonNull Database database, @NonNull String eventRef) {
        mFactory = new DefaultEventViewModel.DefaultEventViewModelFactory();
        mFactory.setDatabase(database);
        mFactory.setEventRef(eventRef);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        mBinding = FragmentDefaultEventBinding.inflate(inflater, container, false);

        return mBinding.getRoot();
   }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Bundle args = getArguments();
        if(args != null) {
            mFactory.setEventRef(args.getString(UIConstants.BUNDLE_EVENT_REF));
        }

        mViewModel = new ViewModelProvider(this, mFactory).get(DefaultEventViewModel.class);
        mEventSharing = new SharingBuilder().setRef(mViewModel.getEventRef()).build();
        mBinding.sharingButton.setOnClickListener(v->startActivity(mEventSharing.getShareIntent()));

        mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
            mBinding.date.setText(event.getDateStr());
            mBinding.description.setText(event.getDescription());
            mBinding.title.setText(event.getTitle());
            mBinding.address.setText(event.getAddress());
            Glide.with(getContext()).load(event.getImageId()).into(mBinding.imageView);
        });

        mBinding.chatButton.setOnClickListener(v->{
            getActivity().getSupportFragmentManager().beginTransaction().replace(this.getId(), ChatFragment.getInstance(mViewModel.getEventRef())).addToBackStack(null).commit();
        });

        mBinding.calendarButton.setOnClickListener(v->{
            startActivityForResult(getCalendarIntent(),LAUNCH_CALENDAR);
                });


        initMinimap(savedInstanceState);

    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == LAUNCH_CALENDAR) {
            if(resultCode != Activity.RESULT_OK){
                Toast.makeText(getContext(), "No Calendar app found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void initMinimap(@Nullable Bundle savedInstanceState) {
        mMapView = mBinding.getRoot().findViewById(R.id.minimap);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(googleMap -> {

            mViewModel.addMapManager(new GoogleMapManager(googleMap));
            mViewModel.getEvent().observe(getViewLifecycleOwner(), event -> {
                mViewModel.setEventOnMap(event.getLocation(), event.getTitle(), mZoomLevel);
           });

        });

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    private Intent getCalendarIntent(){
        Intent intent = new Intent(Intent.ACTION_INSERT);
        intent.setType("vnd.android.cursor.item/event");
        intent.putExtra(CalendarContract.Events.TITLE, mBinding.title.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, mBinding.address.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, mBinding.description.getText().toString());
        String[] date = mBinding.date.getText().toString().split("/");
        GregorianCalendar calDate = new GregorianCalendar(Integer.parseInt(date[2]),
                                                            Integer.parseInt(date[1]),
                                                                Integer.parseInt(date[0]));
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, calDate.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, calDate.getTimeInMillis()+1);

        return  intent;
    }
}
