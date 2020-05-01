package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.Marker;

import java.util.Collections;
import java.util.Dictionary;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.List;
import java.util.Set;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the map that displays events markers
 */
public class MapViewModel extends ViewModel {

    /**
     * Factory for the MapViewModel
     */
    static class MapViewModelFactory extends ParameterizedViewModelFactory {
        /**
         * Constructor of the MapViewModel factory
         */
        MapViewModelFactory() {
            super(MapManager.class, LocationService.class);
        }

        /**
         * Method to set the map manager to the MapViewModel factory
         *
         * @param mapManager {@link ch.epfl.sdp.map.MapManager}
         */
        void setMapManager(@NonNull MapManager mapManager ){
            setValue(0,verifyNotNull(mapManager));
        }

        /**
         * Method to set the location service to the MapViewModel factory
         *
         * @param locationService {@link ch.epfl.sdp.map.LocationService}
         */
        void setLocationService(@NonNull LocationService locationService ){
            setValue(1,verifyNotNull(locationService));
        }
    }
    private MapManager<Marker> mMapManager;
    private LocationService mLocationService;
    private HashMap<Marker, Event> mEventsMarkers = new HashMap<>();

    /**
     * Constructor of the MapViewModel, the factory should be used instead of this
     *
     * @param mapManager {@link ch.epfl.sdp.map.MapManager}
     * @param locationService {@link ch.epfl.sdp.map.LocationService}
     */
    public MapViewModel(@NonNull MapManager<Marker> mapManager,  @NonNull LocationService locationService) {
        mMapManager = mapManager;
        mLocationService = locationService;
    }

    /**
     * Method to focus the camera on the last known location of the device
     *
     * @param context the environment the application is currently running in
     * @param zoomLevel the zoom for the focus
     */
    public void centerCamera(Context context, float zoomLevel) {
        Location location = mLocationService.getLastKnownLocation(context);
        mMapManager.moveCamera(location, zoomLevel);
    }

    /**
     * Method to add a new event marker on the map
     *
     * @param event to be added as a marker on the map
     */
    public void addEvent(Event event) {
        mEventsMarkers.put(mMapManager.addMarker(event.getTitle(), event.getLocation()), event);
    }

    /**
     * Method to clear all the markers
     */
    public void clearEvents() {
        Set<Marker> markers = mEventsMarkers.keySet();
        for (Marker marker : markers )
            marker.remove();
        mEventsMarkers = new HashMap<>();
    }

    /**
     * Method to get an event from its marker
     *
     * @param marker from which we want to get the event
     * @return the corresponding event
     */
    public Event getEventFromMarker(Marker marker) {
        return mEventsMarkers.get(marker);
    }

}
