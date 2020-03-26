package ch.epfl.sdp.ui.main.map;

import com.google.android.gms.maps.model.MarkerOptions;

public interface MapProvider {

    void setMyLocationButtonEnabled(boolean enabled);

    void setMyLocationEnabled(boolean enabled);

    void addMarker(MarkerOptions markerOptions);

}
