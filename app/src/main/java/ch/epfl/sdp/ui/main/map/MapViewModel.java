package ch.epfl.sdp.ui.main.map;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;

import java.util.List;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

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
    }

    public LiveData<List<Event>> getEvents() {
        mEventsLive = mCollectionQuery.liveData(Event.class);
        return mEventsLive;
    }

    public void addMarkers(LifecycleOwner lifecycleOwner) {
        getEvents().observe(lifecycleOwner, events -> {
            for(Event event: events){
                mMapManager.addMarker(event.getTitle(), event.getLocation());
            }
        });
    }

    public void moveCamera(Location location, float zoomLevel) {
        mMapManager.moveCamera(location, zoomLevel);
    }

    public void moveCameraDefault() {
        Location location = new Location("Europe");
        location.setLatitude(46.520564);
        location.setLongitude(6.567827);
        this.moveCamera(location, 4);
    }

    public void setMyLocation() {
        mMapManager.setMyLocation();
    }

//    public void setMapManager(MapManager mapManager) {
//        this.mMapManager = mapManager;
//    }
}
