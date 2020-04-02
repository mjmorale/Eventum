package ch.epfl.sdp.ui.main.map;

import android.location.Location;
import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.model.Marker;
import java.util.Dictionary;
import java.util.Hashtable;
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
            Log.d("factory", "factory used");
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
    private Dictionary<Marker, Event> mEventsMarkers;

    public MapViewModel(@NonNull Database database, @NonNull MapManager mapManager) {
        mDatabase = verifyNotNull(database);
        mCollectionQuery = database.query("events");
        mMapManager = verifyNotNull(mapManager);
        mEventsMarkers = new Hashtable<Marker, Event>();
    }

    private LiveData<List<Event>> getEvents() {
        mEventsLive = mCollectionQuery.liveData(Event.class);
        return mEventsLive;
    }

    public void addMarkers(LifecycleOwner lifecycleOwner) {
        getEvents().observe(lifecycleOwner, events -> { for(Event e: events) addEvent(mMapManager.addMarker(e.getTitle(), e.getLocation()), e);});
    }

    public void moveCamera(Location location, float zoomLevel) {
        mMapManager.moveCamera(location, zoomLevel);
    }

    public void setMyLocation() {
        mMapManager.setMyLocation();
    }

    public void addEvent(Marker marker, Event event) {
        mEventsMarkers.put(marker, event);
    }

    public Event getEventFromMarker(Marker marker) {
        return  mEventsMarkers.get(marker);
    }
}
