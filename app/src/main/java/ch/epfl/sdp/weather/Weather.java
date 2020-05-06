package ch.epfl.sdp.weather;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Weather {

    private String dataString;
    private JsonObject gson;

    private final long SECONDS_IN_DAY = 24 * 3600;
    private final long SECONDS_IN_HALF_DAY = 12 * 3600;


    public Weather(String data) {
        dataString = data;
        gson  = new JsonParser().parse(dataString).getAsJsonObject();

    }

    public double getTemp(int day) {
        JsonObject dayData = getDayJson(day);

        return dayData.getAsJsonObject("temp").get("day").getAsDouble();
    }

    public double getFeelsLikeTemp(int day) {
        JsonObject dayData = getDayJson(day);

        return dayData.getAsJsonObject("feels_like").get("day").getAsDouble();
    }

    public Map<String, Object> getWeather(int day) {

        JsonObject weatherData = getDayJson(day).getAsJsonArray("weather").get(0).getAsJsonObject();

        HashMap<String, Object> weatherMap = new HashMap<>();

        weatherMap.put("id", weatherData.get("id").getAsInt());
        weatherMap.put("main", weatherData.get("main").getAsString());
        weatherMap.put("description", weatherData.get("description").getAsString());
        weatherMap.put("icon", weatherData.get("icon").getAsString());

        return weatherMap;
    }

    public String getString() {
        return dataString;
    }

    public boolean updatedRecently(Date date) {
        long currentTime = DateToSecs(date);

        long response = this.getResponseTimestamp();

        return currentTime < (response + SECONDS_IN_DAY);
    }

    public int getClosestDay(Date date) {
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

    public boolean isForecastAvailable(Date date) {
        long currentTime = DateToSecs(date);

        ArrayList<Long> timestamps = new ArrayList<>(getForecastTimestamps());
        long minTime = timestamps.get(0) - SECONDS_IN_HALF_DAY;
        long maxTime = timestamps.get(timestamps.size() - 1) + SECONDS_IN_HALF_DAY;

        return currentTime >= minTime && currentTime <= maxTime;
    }

    private static long DateToSecs(Date date) {
        return date.getTime() / 1000;
    }

    private long getResponseTimestamp() {
        long time = gson.getAsJsonObject("current").get("dt").getAsLong();

        return time;
    }

    private JsonObject getDayJson(int day) {
        return gson.getAsJsonArray("daily").get(day).getAsJsonObject();
    }

    private List<Long> getForecastTimestamps() {
       ArrayList<Long> timestamps = new ArrayList<>();

       JsonArray dailyData = gson.getAsJsonArray("daily");

        for (int i = 0; i <= 7; i++) {
            long time = dailyData.get(i).getAsJsonObject().get("dt").getAsLong();
            timestamps.add(time);
        }

        return timestamps;
    }

}
