package ch.epfl.sdp.map;

import android.content.Context;
import android.location.Location;

public interface LocationService {
    Location getLastKnownLocation(Context context);

    boolean isLocationEnabled();
}
