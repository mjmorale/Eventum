package ch.epfl.sdp.weather;

import android.content.Context;

import com.android.volley.RequestQueue;


public interface WeatherFetcher {

    interface onResponseCallback {
        void onSuccess(Weather weather);
        void onFailure();
    }

    void fetch(Context context, WeatherFetcher.onResponseCallback callback);
}
