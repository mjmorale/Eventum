package ch.epfl.sdp.ui.main.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

// This is an example of a minimal implementation of a ViewModel.
public class MapViewModel extends ViewModel {

    static class MapViewModelFactory extends ParameterizedViewModelFactory {

        MapViewModelFactory() {
            super(Database.class, MapManager.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setMapManager(@NonNull MapManager mapManager ){setValue(1,verifyNotNull(mapManager));}
    }

    private LiveData<List<Event>> mEventsLive;
    private final Database mDatabase;
    private MapManager mMapManager;
    private CollectionQuery mCollectionQuery;


    public MapViewModel(@NonNull Database database, @NonNull MapManager mapManager) {
        mDatabase = database;
        mCollectionQuery = database.query("events");
        mMapManager = mapManager;

<<<<<<< HEAD
        mEventsLive.observeForever(events -> {
=======
    public LiveData<List<Event>> getEvents() {
        mEventsLive = mCollectionQuery.liveData(Event.class);
        return mEventsLive;
    }

    public void addMarkers(LifecycleOwner lifecycleOwner) {
        getEvents().observe(lifecycleOwner, events -> {
>>>>>>> b9adddfd3309de70cd3cc8b973c7fdbbca630ebd
            for(Event event: events){
                mMapManager.addMarker(event.getTitle(), event.getLocation());
            }
        });
        LatLng europe = new LatLng(46.520564,6.567827);
        moveCamera(europe, 4);
    }


    public void moveCamera(LatLng location, float zoomLevel) {
        mMapManager.moveCamera(location, zoomLevel);
    }


    public void setMyLocationEnabled() {
        mMapManager.setMyLocationEnabled();
    }
}
