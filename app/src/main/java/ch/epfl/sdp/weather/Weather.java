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

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Weather forecast for an entire week at a specific location
 */
public class Weather {

    private String dataString;
    private JsonObject gson;

    private final long SECONDS_IN_HALF_DAY = 12 * 3600;


    /**
     * Constructor for the weather class
     *
     * @param json string of json containing the weather forecast
     */
    public Weather(@NonNull String json) {
        verifyNotNull(json);
        dataString = json;
        gson = new JsonParser().parse(dataString).getAsJsonObject();

    }

    /**
     * Gets the average temperature at a given day
     *
     * @param day day closest to the event date, can be obtained using getClosestDay(Date date)
     * @return average temperature
     */
    public double getTemp(int day) {
        JsonObject dayData = getDayJson(day);

        return dayData.getAsJsonObject("temp").get("day").getAsDouble();
    }

    /**
     * Gets the feels like temperature at a given day
     *
     * @param day day closest to the event date
     * @return feels like temperature
     */
    public double getFeelsLikeTemp(int day) {
        JsonObject dayData = getDayJson(day);

        return dayData.getAsJsonObject("feels_like").get("day").getAsDouble();
    }

    /**
     * Gets the weather forecast (i.e. sunny, rain, etc...) at a given day
     *
     * @param day day closest to the event date
     * @return  map containing the main weather's name, a description of the weather and the weather's associated icon
     */
    public Map<String, Object> getWeather(int day) {

        JsonObject weatherData = getDayJson(day).getAsJsonArray("weather").get(0).getAsJsonObject();

        HashMap<String, Object> weatherMap = new HashMap<>();

        weatherMap.put("main", weatherData.get("main").getAsString());
        weatherMap.put("description", weatherData.get("description").getAsString());
        weatherMap.put("icon", Weather.getWeatherIcon(weatherData.get("icon").getAsString()));

        return weatherMap;
    }


    /**
     * Gets the entire weather forecast as a string
     *
     * @return the entire weather forecast
     */
    public String getString() {

        return dataString;
    }

    /**
     * Checks if the weather was updated recently (within the past 6 hours)
     *
     * @param date the current time
     * @return True if the event was updated recently, False otherwise
     */
    public boolean updatedRecently(@NonNull Date date) {
        verifyNotNull(date);
        long currentTime = dateToSecs(date);

        long response = this.getResponseTimestamp();

        return currentTime < (response + (SECONDS_IN_HALF_DAY / 2));
    }

    /**
     * Gets the closest forecast day for a given date
     *
     * @param date date at which the event is happening
     * @return the closest forecast day if the forecast is available, -1 otherwise
     */
    public int getClosestDay(@NonNull Date date) {
        verifyNotNull(date);
        long currentTime = dateToSecs(date);

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

    /**
     * Checks if the forecast is available at a given date
     *
     * @param date date at which the event is happening
     * @return true if the forecast is available, false otherwise
     */
    public boolean isForecastAvailable(@NonNull Date date) {
        verifyNotNull(date);
        long currentTime = dateToSecs(date);

        ArrayList<Long> timestamps = new ArrayList<>(getForecastTimestamps());
        long minTime = timestamps.get(0) - SECONDS_IN_HALF_DAY;
        long maxTime = timestamps.get(timestamps.size() - 1) + SECONDS_IN_HALF_DAY;

        return currentTime >= minTime && currentTime <= maxTime;
    }

    /**
     * Gets the timestamp at which the weather forecast was fetched
     *
     * @return timestamp in seconds from unix epoch
     */
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

    private static long dateToSecs(Date date) {
        return date.getTime() / 1000;
    }
}
