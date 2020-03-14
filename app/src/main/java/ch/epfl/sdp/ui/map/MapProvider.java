package ch.epfl.sdp.ui.map;

import com.google.android.gms.maps.model.MarkerOptions;

public interface MapProvider {

    void setMyLocationButtonEnabled(Boolean enabled);

    void setMyLocationEnabled(Boolean enabled);

    void addMarker(MarkerOptions markerOptions);

}
