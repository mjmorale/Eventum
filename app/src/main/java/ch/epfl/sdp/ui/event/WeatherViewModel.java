package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

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

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class WeatherViewModel extends ViewModel {

    static class WeatherViewModelFactory extends DatabaseViewModelFactory {

        /**
         * Constructor of the WeatherViewModel factory
         */
        WeatherViewModelFactory() {
            super(String.class);
        }

        /**
         * Method to set the reference of an event to the factory
         *
         * @param eventRef the reference of the event that is currently displayed
         */
        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

    }

    private CollectionQuery mWeatherCollection;
    private FilterQuery mOrderedWeatherQuery;
    private LiveData<List<DatabaseObject<Weather>>> mWeatherLiveData;
    private Observer<List<DatabaseObject<Weather>>> observer;

    /**
     * Constructor of the WeatherViewModel, the factory should be used instead of this
     *
     * @param eventRef the reference of the event to display
     * @param database The database service to use
     */
    public WeatherViewModel(@NonNull String eventRef, @NonNull Database database) {
        mWeatherCollection = database.query("events").document(eventRef).collection("weathers");
        mOrderedWeatherQuery = mWeatherCollection.orderBy("date");
        mWeatherLiveData = mOrderedWeatherQuery.liveData(Weather.class);

        // Automatically delete all out of date weather data
        Comparator<DatabaseObject<Weather>> compareByTimestampReverse =
                (w1, w2) -> Long.compare(w2.getObject().getResponseTimestamp(), w1.getObject().getResponseTimestamp());

        observer = databaseObjects -> {
            if (databaseObjects != null && databaseObjects.size() > 1) {
                ArrayList<DatabaseObject<Weather>> oldWeather = new ArrayList<>(databaseObjects);
                Collections.sort(oldWeather, compareByTimestampReverse);
                oldWeather.remove(0);

                for (DatabaseObject o : oldWeather) {
                    mWeatherCollection.document(o.getId()).delete(callback -> {});
                }
            }
        };

        mWeatherLiveData.observeForever(observer);
    }

    /**
     * Adds weather data to the event in database
     *
     * @param weather weather to add to the event
     */
    public void add(@NonNull Weather weather) {
        mWeatherCollection.create(weather, res -> {});
    }

    /**
     * Gives a list of the weather data associated with an event, sorted by when they were updated
     *
     * @return the weather list associated with the event
     */
    public LiveData<List<DatabaseObject<Weather>>> getWeatherList() {
        return mWeatherLiveData;
    }

    @Override
    public void onCleared() {
        mWeatherLiveData.removeObserver(observer);
        super.onCleared();

    }

}
