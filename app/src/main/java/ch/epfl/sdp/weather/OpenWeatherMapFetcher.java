package ch.epfl.sdp.weather;

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
    private String apiK;

    public OpenWeatherMapFetcher(LatLng location) {
        mLocation = location;
        apiK = "7c35ebdff45e1d54db43876dfcb0c320";
    }

    @Override
    public Weather getWeather() throws Exception {
        String url = getURL();
        String data = getDataFromURL(url);
        return new Weather(data);
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

