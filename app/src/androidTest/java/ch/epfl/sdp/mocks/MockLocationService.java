package ch.epfl.sdp.mocks;

import android.content.Context;
import android.location.Location;

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
    public boolean isLocationEnabled() {
        return true;
    }
}
