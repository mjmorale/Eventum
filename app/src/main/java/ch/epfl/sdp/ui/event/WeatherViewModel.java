package ch.epfl.sdp.ui.event;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.weather.Weather;
import ch.epfl.sdp.weather.WeatherFetcher;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * ViewModel of the weather forecast for an event
 */
public class WeatherViewModel extends ViewModel {

    /**
     * Factory for WeatherViewModel
     */
    static class WeatherViewModelFactory extends DatabaseViewModelFactory {

        /**
         * Argument-free constructor of the WeatherViewModel factory used to define the object types used in the ViewModel
         */
        WeatherViewModelFactory() {
            super(String.class, WeatherFetcher.class);
        }

        /**
         * Method to set the reference of an event to the factory
         *
         * @param eventRef the reference of the event that is currently displayed
         */
        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

        /**
         * Method to set the weather fetcher of the factory
         *
         * @param fetcher fetcher used to get weather forecast
         */
        void setWeatherFetcher(@NonNull WeatherFetcher fetcher) {
            setValue(1, verifyNotNull(fetcher));
        }

    }

    private CollectionQuery mWeatherCollection;
    private FilterQuery mOrderedWeatherQuery;
    private LiveData<List<DatabaseObject<Weather>>> mWeatherLiveData;
    private WeatherFetcher mWeatherFetcher;

    /**
     * Constructor of the WeatherViewModel, the factory should be used instead of this
     *
     * @param eventRef the reference of the event to display
     * @param database The database service to use
     */
    public WeatherViewModel(@NonNull String eventRef, @NonNull WeatherFetcher fetcher, @NonNull Database database) {
        mWeatherCollection = database.query("events").document(eventRef).collection("weathers");
        mOrderedWeatherQuery = mWeatherCollection.orderBy("date");
        mWeatherLiveData = mOrderedWeatherQuery.liveData(Weather.class);
        mWeatherFetcher = fetcher;
    }

    /**
     * Adds weather data to the event in database
     *
     * @param weather weather to add to the event
     */
    public void add(@NonNull Weather weather) {
        mWeatherCollection.create(weather, res -> {
            List<DatabaseObject<Weather>> databaseObjects = mOrderedWeatherQuery.liveData(Weather.class).getValue();
            deleteOldWeather(databaseObjects);
        });
    }

    /**
     * Gives a list of the weather data associated with an event, sorted by when they were updated
     *
     * @return the weather list associated with the event
     */
    public LiveData<List<DatabaseObject<Weather>>> getWeatherList() {
        return mWeatherLiveData;
    }

    /**
     * Updates the weather
     *
     * @param context the environment the application is currently running in
     * @param location the location of the event
     */
    public void updateWeather(Context context, @NonNull LatLng location)  {
        verifyNotNull(location);

        WeatherFetcher.onResponseCallback responseCallback = new WeatherFetcher.onResponseCallback() {
            @Override
            public void onSuccess(Weather weather) {
                add(weather);
            }

            @Override
            public void onFailure() {
                Toast.makeText(context, "Weather API is currently unavailable", Toast.LENGTH_SHORT).show();
            }
        };

        mWeatherFetcher.fetch(context, responseCallback, location);
    }

    @Override
    public void onCleared() {
        super.onCleared();

    }

    private void deleteOldWeather(List<DatabaseObject<Weather>> databaseObjects) {
        // Delete all out of date weather data
        Comparator<DatabaseObject<Weather>> compareByTimestampReverse =
                (w1, w2) -> Long.compare(w2.getObject().getResponseTimestamp(), w1.getObject().getResponseTimestamp());

        if (databaseObjects != null && databaseObjects.size() > 1) {
            ArrayList<DatabaseObject<Weather>> oldWeather = new ArrayList<>(databaseObjects);
            Collections.sort(oldWeather, compareByTimestampReverse);
            oldWeather.remove(0);

            for (DatabaseObject o : oldWeather) {
                mWeatherCollection.document(o.getId()).delete(callback -> {});
            }
        }

    }

}
