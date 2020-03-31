package ch.epfl.sdp.ui.main.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.map.MapProvider;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

// This is an example of a minimal implementation of a ViewModel.
public class MapViewModel extends ViewModel {

    static class MapViewModelFactory extends ParameterizedViewModelFactory {

        MapViewModelFactory() {
            super(Database.class, MapProvider.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setMapProvider(@NonNull MapProvider mapProvider){setValue(1,verifyNotNull(mapProvider));}
    }

    private LiveData<List<Event>> mEventsLive;
    private final Database mDatabase;
    private MapProvider mMapProvider;


    public MapViewModel(@NonNull Database database, @NonNull MapProvider mapProvider) {
        mDatabase = database;
        mEventsLive = mDatabase.query("events").liveData(Event.class);
        mMapProvider = mapProvider;
    }

    public LiveData<List<Event>> getEvents() {
        return mEventsLive;
    }


    public void addMarker(Event event) {
            LatLng coordinates = event.getLocation();
            String name = event.getTitle();
            mMapProvider.addMarker(new MarkerOptions().position(coordinates).title(name));
    }
}
