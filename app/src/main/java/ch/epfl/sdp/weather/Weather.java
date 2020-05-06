package ch.epfl.sdp.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Weather {

    private String dataString;
    private JSONObject data;

    private final long SECONDS_IN_DAY = 24 * 3600;
    private final long SECONDS_IN_HALF_DAY = 12 * 3600;


    public Weather(String data) throws JSONException {
        dataString = data;
        this.data = new JSONObject(data);
    }

    public double getTemp(int day) throws JSONException {
        JSONObject dayData = getDayJSON(day);

        return dayData.getJSONObject("temp").getDouble("day");
    }

    public double getFeelsLikeTemp(int day) throws JSONException {
        JSONObject dayData = getDayJSON(day);

        return dayData.getJSONObject("feels_like").getDouble("day");
    }

    public Map<String, Object> getWeather(int day) throws JSONException {
        JSONObject weatherData = getDayJSON(day).getJSONObject("weather");

        HashMap<String, Object> weatherMap = new HashMap<>();

        weatherMap.put("id", weatherData.getInt("id"));
        weatherMap.put("main", weatherData.getString("main"));
        weatherMap.put("description", weatherData.getString("description"));
        weatherMap.put("icon", weatherData.getString("icon"));

        return weatherMap;
    }

    public String getString() {
        return dataString;
    }

    public boolean updatedRecently(Date date) throws JSONException {
        long currentTime = DateToSecs(date);

        long response = this.getResponseTimestamp();

        return currentTime < (response + SECONDS_IN_DAY);
    }

    public int getClosestDay(Date date) throws JSONException {
        long currentTime = DateToSecs(date);

        ArrayList<Long> timestamps = new ArrayList<>(getForecastTimestamps());

        for (int i = 0; i < timestamps.size(); i++) {
            long minDayTime = timestamps.get(i) - SECONDS_IN_HALF_DAY;
            long maxDayTime = timestamps.get(i) + SECONDS_IN_HALF_DAY;

            if (currentTime >= minDayTime && currentTime < maxDayTime) {
                return i;
            }
        }

        return -1;
    }

    public boolean isForecastAvailable(Date date) throws JSONException {
        long currentTime = DateToSecs(date);

        ArrayList<Long> timestamps = new ArrayList<>(getForecastTimestamps());
        long minTime = timestamps.get(0) - SECONDS_IN_HALF_DAY;
        long maxTime = timestamps.get(timestamps.size() - 1) + SECONDS_IN_HALF_DAY;

        return currentTime >= minTime && currentTime <= maxTime;
    }

    private static long DateToSecs(Date date) {
        return date.getTime() / 1000;
    }

    private long getResponseTimestamp() throws JSONException {
        long time = data.getJSONObject("current").getLong("dt");

        return time;
    }

    private JSONObject getDayJSON(int day) throws JSONException {
        return data.getJSONObject(Integer.toString(day));
    }

    private List<Long> getForecastTimestamps() throws JSONException {
       ArrayList<Long> timestamps = new ArrayList<>();

        for (int i = 0; i <= 7; i++) {
            long time = data.getJSONObject(Integer.toString(i)).getLong("dt");
            timestamps.add(time);
        }

        return timestamps;
    }

}
