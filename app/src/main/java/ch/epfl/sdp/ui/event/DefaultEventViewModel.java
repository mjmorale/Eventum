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

/**
 * View model to display some detail information of an event
 */
public class DefaultEventViewModel extends ViewModel {

    /**
     * Factory of the DefaultEventViewModel
     */
    static class DefaultEventViewModelFactory extends DatabaseViewModelFactory {

        /**
         * Constructor of the DefaultEventViewModel factory
         */
        DefaultEventViewModelFactory() {
            super(String.class);
        }

        /**
         * Method to set the reference of an event to the factory
         *
         * @param eventRef the reference of the event to display
         */
        void setEventRef(@NonNull String eventRef) {

            setValue(0, verifyNotNull(eventRef));
        }
    }

    private LiveData<Event> mEvent;

    private MapManager mMapManager;
    private final DocumentQuery mEventDocumentQuery;
    private final Database mDatabase;
    private final String mEventRef;

    /**
     * Constructor of the DefaultEventViewModel, the factory should be used instead of this
     *
     * @param eventRef the reference of the event to display
     * @param database where the events are located
     */
    public DefaultEventViewModel(@NonNull String eventRef, @NonNull Database database) {
        mDatabase = database;
        mEventRef = eventRef;
        mEventDocumentQuery = database.query("events").document(eventRef);
    }

    /**
     * Method to get the specific event in the database
     *
     * @return a live data of this event
     */
    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mEventDocumentQuery.livedata(Event.class);
        }
        return mEvent;
    }

    /**
     * Method to get the reference of the specific event
     *
     * @return the reference of the event
     */
    public String getEventRef(){
        return mEventRef;
    }


    /**
     * Adds a mapManager to the view model, which can then be used to control the minimap
     * @param mapManager the mapManager we want to assign to the view model
     */
    public void addMapManager(MapManager mapManager) {
        if (mMapManager == null) {
            mMapManager =  mapManager;
        }
    }

    /**
     * Adds an event at the given location and centers the map on that location
     * @param latLng the coordinates of the event
     * @param eventName the name of the event
     * @param zoomLevel the zoom level of the map
     * @return boolean indicating whether the event was successfully set on the map or not
     */
    public boolean setEventOnMap(LatLng latLng, String eventName, float zoomLevel) {

        if (mMapManager == null) {
            return false;
        }
        else {
            mMapManager.clear();
            mMapManager.addMarker(eventName, latLng);
            mMapManager.moveCamera(latLng, zoomLevel);
            return true;
        }
    }
}
