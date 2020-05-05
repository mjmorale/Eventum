package ch.epfl.sdp.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;

public class Weather {

    public String dataString;
    public JSONObject data;

    public Weather(String data) throws JSONException {
        dataString = data;

        this.data = new JSONObject(data);
    }

    public long getResponseTimestamp() throws JSONException {
        long time = data.getJSONObject("current").getLong("dt");

        return time;
    }

    private JSONObject getDayJSON(int day) throws JSONException {
        return data.getJSONObject(Integer.toString(day));
    }

    private HashMap<String, Object> getDayInfo(int day) throws JSONException {

        JSONObject dayData = getDayJSON(day);

        HashMap<String, Object> dayInfo = new HashMap<>();
        dayInfo.put("temp", dayData.getJSONObject("temp").getDouble("day"));
        dayInfo.put("feels_like", dayData.getJSONObject("feels_like").getDouble("day"));

        JSONObject weatherData = dayData.getJSONObject("weather");
        dayInfo.put("weatherId",weatherData.getInt("id"));
        dayInfo.put("weatherTitle", weatherData.getString("main"));
        dayInfo.put("weatherDescription", weatherData.getString("description"));
        dayInfo.put("weatherIcon", weatherData.getString("icon"));


        return dayInfo;
    }


    private HashMap<Integer, Long> getForecastTimestamps() throws JSONException {
       HashMap<Integer, Long> timestamps = new HashMap<>();

        for (int i = 0; i <= 7; i++) {
            long time = data.getJSONObject(Integer.toString(i)).getLong("dt");
            timestamps.put(i, time);
        }

        return timestamps;
    }

}
