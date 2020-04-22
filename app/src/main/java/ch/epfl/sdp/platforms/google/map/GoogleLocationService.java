package ch.epfl.sdp.platforms.google.map;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;

import androidx.core.app.ActivityCompat;

import ch.epfl.sdp.map.LocationService;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public final class GoogleLocationService implements LocationService {

    private LocationManager mLocationManager;

    public GoogleLocationService(LocationManager locationManager) {
        verifyNotNull(locationManager);
        mLocationManager = locationManager;
    }

    @Override
    public Location getLastKnownLocation(Context context) {
        int fineLocation =
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION);
        int coarseLocation =
                ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_COARSE_LOCATION);
        if ( fineLocation != PackageManager.PERMISSION_GRANTED || coarseLocation != PackageManager.PERMISSION_GRANTED) {
            throw new IllegalStateException("Insufficient permissions.");
        }
        Location location = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
        if (location == null) {
            Location defaultLocation = new Location("Default");
            defaultLocation.setLatitude(46.520564);
            defaultLocation.setLongitude(6.567827);
            return defaultLocation;
        }
        return location;
    }

    @Override
    public boolean isLocationEnabled() {
        return isLocationEnabled();
    }
}
