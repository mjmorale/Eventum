package ch.epfl.sdp.weather;

import android.content.Context;
import android.location.Location;

import com.google.android.gms.maps.model.LatLng;

public interface WeatherFetcher {

    interface onResponseCallback {
        void onSuccess(Weather weather);
        void onFailure();
    }

    void fetch(Context context, WeatherFetcher.onResponseCallback callback, LatLng location);
}
