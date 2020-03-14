package ch.epfl.sdp.ui.main;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

public interface MapProvider {

    void setMyLocationButtonEnabled(Boolean enabled);

    void setMyLocationEnabled(Boolean enabled);

    void addMarker(MarkerOptions markerOptions);

}
