package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventViewModel extends ViewModel {

    static class DefaultEventViewModelFactory extends DatabaseViewModelFactory {

        DefaultEventViewModelFactory() {
            super(String.class, MapManager.class);
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

        void setMapManager(@NonNull MapManager mapManager) {
            setValue(1, verifyNotNull(mapManager));
        }
    }

    private LiveData<Event> mEvent;

    private MapManager mMapManager;
    private final DocumentQuery mEventDocumentQuery;
    private final String mEventRef;

    public DefaultEventViewModel(@NonNull String eventRef, @NonNull MapManager mapManager, @NonNull Database database) {
        verifyNotNull(eventRef, database);
        mEventRef = eventRef;
        mMapManager = mapManager;
        mEventDocumentQuery = database.query("events").document(eventRef);
    }

    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mEventDocumentQuery.livedata(Event.class);
        }
        return mEvent;
    }

    public String getEventRef(){
        return mEventRef;
    }

    /**
     * Adds an event at the given location and centers the map on that location
     * @param latLng the coordinates of the event
     * @param eventName the name of the event
     * @param zoomLevel the zoom level of the map
     */
    public void setEventOnMap(LatLng latLng, String eventName, float zoomLevel) {
        mMapManager.clear();
        mMapManager.addMarker(eventName, latLng);
        mMapManager.moveCamera(latLng, zoomLevel);
    }
}
