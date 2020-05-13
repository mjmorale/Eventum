package ch.epfl.sdp.platforms.openweathermap;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sdp.R;
import ch.epfl.sdp.weather.Weather;
import ch.epfl.sdp.weather.WeatherFetcher;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Fetches data using OpenWeatherMap's OneCall API, fetching is done asynchronously using Volley
 */
public class OpenWeatherMapFetcher implements WeatherFetcher {

    /**
     * Constructor of OpenWeatherMapFetcher class, doesn't take any arguments
     */
    public OpenWeatherMapFetcher() {}

    @Override
    public void fetch(Context context, WeatherFetcher.onResponseCallback callback, @NonNull LatLng location) {
        verifyNotNull(location);

        String apiK = verifyNotNull(context.getString(R.string.openweather_api_k));

        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getURL(location, apiK);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> callback.onSuccess(new Weather(response)),
                error -> callback.onFailure());

        queue.add(stringRequest);
    }

    private String getURL(@NonNull LatLng location, @NonNull String apiKey) {
        verifyNotNull(apiKey);

        StringBuilder sb = new StringBuilder();
        sb.append("https://api.openweathermap.org/data/2.5/onecall?");
        sb.append("lat=").append(location.latitude).append("&lon=").append(location.longitude);
        sb.append("&exclude=hourly").append("&units=metric");
        sb.append("&appid=").append(apiKey);

        return sb.toString();
    }
}

