package ch.epfl.sdp.weather;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class OpenWeatherMapFetcher implements WeatherFetcher {


    private LatLng mLocation;
    private String apiK = "7c35ebdff45e1d54db43876dfcb0c320";


    public OpenWeatherMapFetcher(LatLng location) {
        mLocation = location;
    }

    @Override
    public void fetch(Context context, WeatherFetcher.onResponseCallback callback) {
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = getURL();

        // Request a string response from the provided URL.
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                response -> callback.onSuccess(new Weather(response)),
                error -> callback.onFailure());

        queue.add(stringRequest);
    }

    private String getURL() {

        StringBuilder sb;
        sb = new StringBuilder();
        sb.append("https://api.openweathermap.org/data/2.5/onecall?");
        sb.append("lat=").append(mLocation.latitude);
        sb.append("&").append("lon=").append(mLocation.longitude);
        sb.append("&").append("&exclude=hourly");
        sb.append("&").append("units=metric");
        sb.append("&").append("appid=").append(apiK);

        return sb.toString();
    }


    private static String getDataFromURL(String urlString) throws IOException {

        URL url = new URL(urlString);

        HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
        urlConnection.setRequestMethod("GET");
        urlConnection.setReadTimeout(10000);
        urlConnection.setConnectTimeout(15000);
        urlConnection.connect();

        BufferedReader br = new BufferedReader(new InputStreamReader(url.openStream()));

        StringBuilder sb = new StringBuilder();

        String line;
        while ((line = br.readLine()) != null) {
            sb.append(line).append("\n");
        }
        br.close();
        urlConnection.disconnect();

        return sb.toString();
    }

}

