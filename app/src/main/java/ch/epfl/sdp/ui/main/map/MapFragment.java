package ch.epfl.sdp.ui.main.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.firebase.firestore.FirebaseFirestore;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.databinding.FragmentMapBinding;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;

// This is an example of a minimal implementation of a Fragment.
public class MapFragment extends Fragment implements OnMapReadyCallback {

    private MapViewModel mViewModel;
    private final MapViewModel.MapViewModelFactory mFactory;
    private FragmentMapBinding mBinding;
    private MapView mMapView;

    // Replace value1 and value2 by your own arguments
    // If the fragment does not require arguments through a Bundle,
    // this method can be removed
//    public static MapFragment getInstance(String value1, String value2) {
//        Bundle bundle = new Bundle();
//        //TODO: replace with your own arguments
//        bundle.putString("value1", value1);
//        bundle.putString("value2", value2);
//
//        MapFragment fragment = new MapFragment();
//        fragment.setArguments(bundle);
//
//        return fragment;
//    }

    // The default constructor is required by Android.
    // E.g., if the fragment is recreated, the default constructor is used.
    // This means that the fragment cannot rely on parameters passed through the constructor
    public MapFragment() {
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setDatabase(new FirestoreDatabase(FirebaseFirestore.getInstance()));
    }

    // This constructor should only be used in tests through a custom fragment factory
    // (for the same reason as mentionned before).
    // You can pass any number of arguments you want and set them manually in the view model factory.
    @VisibleForTesting
    public MapFragment(@NonNull Database database) {
        mFactory = new MapViewModel.MapViewModelFactory();
        mFactory.setDatabase(database);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        // Initializes the view using the view bindings created for your fragment.
        mBinding = FragmentMapBinding.inflate(inflater, container, false);

        mMapView = mBinding.getRoot().findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.getMapAsync(this);

        return mBinding.getRoot();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // If your fragment takes arguments using a Bundle and that the view model requires this value,
        // your can set it in the factory here.
        // If your fragment does not require a Bundle, then you can delete this part
//        Bundle args = getArguments();
//        if(args != null) {
//            //TODO: replace with your own arguments
//            mFactory.setValue1(args.getString("value1"));
//        }

        // This is where the view model factory is called to create a new view model.
        // Make sure your factory is fully initialized before this point.
        mViewModel = new ViewModelProvider(this, mFactory).get(MapViewModel.class);

        mViewModel.getEvents().observe(getViewLifecycleOwner(), events -> {
            for(Event event: events){
                mViewModel.addMarker(event);
            }
        });

    }

    // Forces the deletion of the view binding instance when the view is deleted.
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        mBinding = null;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mViewModel.setMap(googleMap);
    }

    @Override
    public void onResume() {
        mMapView.onResume();
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}