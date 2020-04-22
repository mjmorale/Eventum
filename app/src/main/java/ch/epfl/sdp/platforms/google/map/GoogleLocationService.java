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

    private static GoogleLocationService INSTANCE;

    private GoogleLocationService(LocationManager locationManager) {
        verifyNotNull(locationManager);
        mLocationManager = locationManager;
    }

    public static void initService(LocationManager locationManager) {
        INSTANCE = new GoogleLocationService(locationManager);
    }

    public static GoogleLocationService getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("You must call initService first.");
        }
        return INSTANCE;
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
        if (location == null)
            return new Location("Default");
        return location;
    }

    @Override
    public boolean isLocationEnabled() {
        return isLocationEnabled();
    }
}
