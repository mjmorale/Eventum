package ch.epfl.sdp.platforms.openweathermap;

import android.content.Context;

import androidx.annotation.NonNull;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;
import ch.epfl.sdp.weather.Weather;
import ch.epfl.sdp.weather.WeatherFetcher;

/**
 * Fetches data using OpenWeatherMap's OneCall API, fetching is done asynchronously using Volley
 */
public class OpenWeatherMapFetcher implements WeatherFetcher {

    private String apiK = "7c35ebdff45e1d54db43876dfcb0c320";

    public OpenWeatherMapFetcher() {}

    @Override
    public void fetch(Context context, WeatherFetcher.onResponseCallback callback, @NonNull LatLng location) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getURL(location);

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> callback.onSuccess(new Weather(response)),
                error -> callback.onFailure());

        queue.add(stringRequest);
    }

    private String getURL(@NonNull LatLng location) {

        StringBuilder sb;
        sb = new StringBuilder();
        sb.append("https://api.openweathermap.org/data/2.5/onecall?");
        sb.append("lat=").append(location.latitude).append("&lon=").append(location.longitude);
        sb.append("&exclude=hourly").append("&units=metric");
        sb.append("&appid=").append(apiK);

        return sb.toString();
    }
}

