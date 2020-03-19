package ch.epfl.sdp.ui.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ch.epfl.sdp.R;

public class MapFragment extends Fragment {
    private MapView mMapView;
    private GoogleMapProvider mGoogleMapProvider;
    private MapViewModel mViewModel;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mViewModel = new ViewModelProvider(this).get(MapViewModel.class);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView= view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mGoogleMapProvider = new GoogleMapProvider(this.getContext(), mMapView);
        mGoogleMapProvider.setMyLocationButtonEnabled(true);
        mGoogleMapProvider.setMyLocationEnabled(true);

        mViewModel.getEvents().observe(getViewLifecycleOwner(), event -> {

        });

        addMarker("Vidy", new LatLng(46.518615, 6.591796), mGoogleMapProvider);

        return view;
    }

    public void addMarker(String eventName, LatLng coordinates, GoogleMapProvider googleMapProvider) {
        googleMapProvider.addMarker(new MarkerOptions().position(coordinates).title(eventName));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
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