package ch.epfl.sdp.weather;

import androidx.annotation.NonNull;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.R;

public class Weather {

    private String dataString;
    private JsonObject gson;

    private final long SECONDS_IN_HALF_DAY = 12 * 3600;


    public Weather(@NonNull String data) {
        dataString = data;
        gson = new JsonParser().parse(dataString).getAsJsonObject();

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

        weatherMap.put("main", weatherData.get("main").getAsString());
        weatherMap.put("description", weatherData.get("description").getAsString());
        weatherMap.put("icon", Weather.getWeatherIcon(weatherData.get("icon").getAsString()));

        return weatherMap;
    }

    public String getString() {
        return dataString;
    }

    public boolean updatedRecently(Date date) {
        long currentTime = DateToSecs(date);

        long response = this.getResponseTimestamp();

        return currentTime < (response + (SECONDS_IN_HALF_DAY / 2));
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

    public long getResponseTimestamp() {
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

    private static int getWeatherIcon(String iconName) {
        HashMap<String, Integer> nameToIcon = new HashMap<>();
        nameToIcon.put("01d", R.drawable.weather_01d);
        nameToIcon.put("01n", R.drawable.weather_01n);
        nameToIcon.put("02d", R.drawable.weather_02d);
        nameToIcon.put("02n", R.drawable.weather_02n);
        nameToIcon.put("03d", R.drawable.weather_03d);
        nameToIcon.put("04d", R.drawable.weather_04d);
        nameToIcon.put("04n", R.drawable.weather_04n);
        nameToIcon.put("09d", R.drawable.weather_09d);
        nameToIcon.put("09n", R.drawable.weather_09n);
        nameToIcon.put("10d", R.drawable.weather_10d);
        nameToIcon.put("10n", R.drawable.weather_10n);
        nameToIcon.put("11d", R.drawable.weather_11d);
        nameToIcon.put("11n", R.drawable.weather_11n);
        nameToIcon.put("13d", R.drawable.weather_13d);
        nameToIcon.put("13n", R.drawable.weather_13n);
        nameToIcon.put("50d", R.drawable.weather_50d);
        nameToIcon.put("50n", R.drawable.weather_50n);

        if (nameToIcon.containsKey(iconName)) {
            return nameToIcon.get(iconName);
        }
        else {
            return android.R.color.transparent;
        }
    }

    private static long DateToSecs(Date date) {
        return date.getTime() / 1000;
    }
}
