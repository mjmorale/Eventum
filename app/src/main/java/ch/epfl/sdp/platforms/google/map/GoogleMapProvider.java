package ch.epfl.sdp.platforms.google.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.map.MapProvider;

public class GoogleMapProvider implements MapProvider {
    private boolean mLocationEnabled;
    private List<MarkerOptions> mMarkerOptions;
    private GoogleMap mMap = null;

    public GoogleMapProvider(MapView mapView){
        mMarkerOptions = new ArrayList<>();
        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            setMyLocationEnabled(mLocationEnabled);
            for(MarkerOptions markerOptions: mMarkerOptions) mMap.addMarker(markerOptions);
        });
    }


    @Override
    public void setMyLocationEnabled(boolean enabled) {
        mLocationEnabled = enabled;
    }

    @Override
    public void addMarker(MarkerOptions markerOptions) {
        if(mMap==null){
            mMarkerOptions.add(markerOptions);
        }else{
            mMap.addMarker(markerOptions);
        }
    }
}