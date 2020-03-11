package ch.epfl.sdp.ui.main;


import android.location.Location;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import ch.epfl.sdp.LocationHelper;
import ch.epfl.sdp.R;

public class MapFragment extends Fragment implements OnMapReadyCallback {
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return view;
    }

    @Override
    public void onMapReady(GoogleMap map) {
        float zoom = 12;
        LatLng Vidy = new LatLng(46.518615, 6.591796);
        LatLng Satellite = new LatLng(46.520564, 6.567827);

        map.addMarker(new MarkerOptions().position(Vidy).title("Event A"));
        map.addMarker(new MarkerOptions().position(Satellite).title("Event B"));

        LocationHelper locationHelper = new LocationHelper(getContext());
        locationHelper.startListeningUserLocation(new LocationHelper.MyLocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                LatLng myLocation=new LatLng(location.getLatitude(), location.getLongitude());
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(myLocation, zoom));
            }
        });
    }
}
