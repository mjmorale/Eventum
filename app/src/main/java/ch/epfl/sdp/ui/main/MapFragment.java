package ch.epfl.sdp.ui.main;


import android.location.Location;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import ch.epfl.sdp.LocationHelper;
import ch.epfl.sdp.R;


public class MapFragment extends Fragment implements OnMapReadyCallback{
    private MapView mapView;
    private GoogleMap map;
    private LocationHelper locationHelper;
    private Location myLocation;
    private boolean firstView;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        locationHelper = new LocationHelper(getContext());
        firstView=true;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        mapView= (MapView) view.findViewById(R.id.mapView2);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);

        return view;
    }

    @Override
    public void onMapReady(GoogleMap googlemap) {

        float zoom = 12;
        LatLng Satellite = new LatLng(46.520564, 6.567827);

        locationHelper.startListeningUserLocation(new LocationHelper.MyLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                myLocation= location;
                if(firstView){
                    LatLng currentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,zoom));
                    firstView=false;
                }
            }
        });

        map = googlemap;
        map.getUiSettings().setMyLocationButtonEnabled(true);
        map.setMyLocationEnabled(true);
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(Satellite,zoom));
        map.addMarker(new MarkerOptions().position(Satellite).title("Event B"));
        map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
            @Override
            public boolean onMyLocationButtonClick() {
                if(myLocation!=null){
                    LatLng currentLocation = new LatLng(myLocation.getLatitude(),myLocation.getLongitude());
                    map.animateCamera(CameraUpdateFactory.newLatLngZoom(currentLocation,12));
                }
                return true;
            }
        });
    }

    @Override
    public void onResume() {
        mapView.onResume();
        super.onResume();
    }


    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }

}
