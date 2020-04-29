package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import java.util.Objects;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventViewModel extends ViewModel {

    static class DefaultEventViewModelFactory extends DatabaseViewModelFactory {

        DefaultEventViewModelFactory() {
            super(String.class);
        }

        void setEventRef(@NonNull String eventRef) {

            setValue(0, verifyNotNull(eventRef));
        }
    }

    private LiveData<Event> mEvent;

    private MapManager mMapManager;
    private final DocumentQuery mEventDocumentQuery;
    private final Database mDatabase;
    private final String mEventRef;

    public DefaultEventViewModel(@NonNull String eventRef, @NonNull Database database) {
        mDatabase = database;
        mEventRef = eventRef;
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

    public void addMapManager(MapManager mapManager) {
        if (mMapManager == null) {
            mMapManager =  mapManager;
        }
    }

    public void setEventOnMap(LatLng latLng, String eventName, float zoomLevel) {
        verifyNotNull(mMapManager);
        mMapManager.clear();
        mMapManager.addMarker(eventName, latLng);
        mMapManager.moveCamera(latLng, zoomLevel);
    }
}
