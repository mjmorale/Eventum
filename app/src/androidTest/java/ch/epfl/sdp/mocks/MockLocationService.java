package ch.epfl.sdp.mocks;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sdp.map.LocationService;

public class MockLocationService implements LocationService {
    @Override
    public Location getLastKnownLocation(Context context) {
        Location location = new Location("Europe");
        location.setLatitude(46.520564);
        location.setLongitude(6.567827);
        return location;
    }

    @Override
    public float distanceTo(Context context, LatLng location) {
        return 2000.0f;
    }
}
