package ch.epfl.sdp.weather;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

public class Weather {

    private String dataString;
    private JSONObject data;

    private final long SECONDS_IN_DAY = 24 * 3600;
    private final long SECONDS_IN_HALF_DAY = 12 * 3600;


    public Weather(String data) throws JSONException {
        dataString = data;
        this.data = new JSONObject(data);
    }

    public long getResponseTimestamp() throws JSONException {
        long time = data.getJSONObject("current").getLong("dt");

        return time;
    }


    public String getString() {
        return dataString;
    }

    private boolean updatedRecently(Date date) throws JSONException {
        long currentTime = date.getTime() / 1000;

        long response = this.getResponseTimestamp();

        return currentTime < (response + SECONDS_IN_DAY);
    }

    private int getClosestDay(Date date) throws JSONException {
        long currentTime = date.getTime() / 1000;

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

    private boolean isForecastAvailable(Date date) throws JSONException {
        long currentTime = date.getTime() / 1000;

        ArrayList<Long> timestamps = new ArrayList<>(getForecastTimestamps());
        long minTime = timestamps.get(0) - SECONDS_IN_HALF_DAY;
        long maxTime = timestamps.get(timestamps.size() - 1) + SECONDS_IN_HALF_DAY;

        return currentTime >= minTime && currentTime <= maxTime;
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

    private List<Long> getForecastTimestamps() throws JSONException {
       ArrayList<Long> timestamps = new ArrayList<>();

        for (int i = 0; i <= 7; i++) {
            long time = data.getJSONObject(Integer.toString(i)).getLong("dt");
            timestamps.add(time);
        }

        return timestamps;
    }

}
