package ch.epfl.sdp;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import androidx.core.app.ActivityCompat; // you can change it to appsupport if you are not using androidx
import androidx.core.content.ContextCompat;
import static android.content.Context.LOCATION_SERVICE;

// source: https://handyopinion.com/utility-class-to-fetch-current-location-through-gps-in-android-java/
public class LocationHelper {

    final int LOCATION_REFRESH_TIME = 3000; // 3 seconds. The Minimum Time to get location update
    final int LOCATION_REFRESH_DISTANCE = 30; // 30 meters. The Minimum Distance to be changed to get location update
    final int MY_PERMISSIONS_REQUEST_LOCATION = 100;

    Context context;
    MyLocationListener myLocationListener;

    public interface MyLocationListener {
        public void onLocationChanged(Location location);
    }

    public LocationHelper (Context context) {
        this.context = context;
    }

    public void startListeningUserLocation(final MyLocationListener myListener) {
        myLocationListener = myListener;

        LocationManager mLocationManager = (LocationManager) context.getSystemService(LOCATION_SERVICE);

        final LocationListener mLocationListener = new LocationListener() {
            @Override
            public void onLocationChanged(final Location location) {
                //your code here
                myLocationListener.onLocationChanged(location);
            }
            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }
            @Override
            public void onProviderEnabled(String provider) {
            }
            @Override
            public void onProviderDisabled(String provider) {
            }
        };

        if (ContextCompat.checkSelfPermission( context,android.Manifest.permission.ACCESS_COARSE_LOCATION ) == PackageManager.PERMISSION_GRANTED &&  ContextCompat.checkSelfPermission( context, Manifest.permission.ACCESS_FINE_LOCATION ) == PackageManager.PERMISSION_GRANTED)
        {
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, LOCATION_REFRESH_TIME,
                    LOCATION_REFRESH_DISTANCE, mLocationListener);
        } else {
            if (ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_FINE_LOCATION) || ActivityCompat.shouldShowRequestPermissionRationale((Activity) context, Manifest.permission.ACCESS_COARSE_LOCATION)) {
                // permission is denined by user, you can show your alert dialog here to send user to App settings to enable permission
            } else {
                ActivityCompat.requestPermissions((Activity) context,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                        MY_PERMISSIONS_REQUEST_LOCATION);
            }
        }
    }
}
