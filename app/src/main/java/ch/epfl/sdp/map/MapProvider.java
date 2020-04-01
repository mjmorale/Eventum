package ch.epfl.sdp.map;

import com.google.android.gms.maps.model.MarkerOptions;

public interface MapProvider {

    void setMyLocationEnabled(boolean enabled);

    void addMarker(MarkerOptions markerOptions);

}
