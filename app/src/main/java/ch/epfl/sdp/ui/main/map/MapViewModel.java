package ch.epfl.sdp.ui.main.map;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.model.Marker;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.platforms.google.map.GoogleMapManager;
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

        void setMapManager(@NonNull MapManager mapManager ){
            setValue(1,verifyNotNull(mapManager));
        }
    }

    private LiveData<List<Event>> mEventsLive;
    private MapManager<Marker> mMapManager;
    private Dictionary<Marker, Event> mEventsMarkers = new Hashtable<>();;
    private final Observer<List<Event>> mEventObserver;

    public MapViewModel(@NonNull Database database, @NonNull MapManager<Marker> mapManager) {
        verifyNotNull(database);
        verifyNotNull(mapManager);
        mMapManager = mapManager;

        mEventsLive = database.query("events").liveData(Event.class);
        mEventObserver = events -> { for(Event e: events) { addEvent(mMapManager.addMarker(e.getTitle(), e.getLocation()), e);}};
        mEventsLive.observeForever(mEventObserver);
    }

    public void moveCamera(Location location, float zoomLevel) {
        mMapManager.moveCamera(location, zoomLevel);
    }

    public void addEvent(Marker marker, Event event) {
        mEventsMarkers.put(marker, event);
    }

    public Event getEventFromMarker(Marker marker) {
        return mEventsMarkers.get(marker);
    }

    @Override
    protected void onCleared() { mEventsLive.removeObserver(mEventObserver);}
}
