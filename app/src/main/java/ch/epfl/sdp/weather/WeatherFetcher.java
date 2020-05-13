package ch.epfl.sdp.weather;

import android.content.Context;
import com.google.android.gms.maps.model.LatLng;

/**
 * Fetcher to get a weather forecast at a given location
 */
public interface WeatherFetcher {

    /**
     * Callback to use once fetching is complete
     */
    interface onResponseCallback {
        void onSuccess(Weather weather);
        void onFailure();
    }

    /**
     * Fetches the weather asynchronously
     *
     * @param context the environment the application is currently running in
     * @param callback callback to indicate whether fetching was successful or not, and return the weather forecast if it was
     * @param location the location of the weather forecast
     */
    void fetch(Context context, WeatherFetcher.onResponseCallback callback, LatLng location);
}
