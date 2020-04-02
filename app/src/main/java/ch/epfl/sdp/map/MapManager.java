package ch.epfl.sdp.map;

import android.location.Location;
import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;

public interface MapManager<TMarker> {

    TMarker addMarker(@NonNull String title, @NonNull LatLng location);

    void moveCamera(@NonNull Location location, float zoomLevel);
}
