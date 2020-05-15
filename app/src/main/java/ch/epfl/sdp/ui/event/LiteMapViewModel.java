package ch.epfl.sdp.ui.event;

import com.google.android.gms.maps.model.LatLng;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * ViewModel that manages a MapView in lite mode.
 */
public class LiteMapViewModel extends ViewModel {

    /**
     *  Factory for the LiteMapViewModel
     */
    public static class LiteMapViewModelFactory extends ParameterizedViewModelFactory {

        /**
         * Empty constructor for the factory
         */
        public LiteMapViewModelFactory() {
            super();
        }
    }

    /**
     * Construct a new LiteMapViewModel. This is for testing purposes, please use
     * the factory instead.
     */
    public LiteMapViewModel() {}

    /**
     * Adds an event at the given location and centers the map on that location
     * @param mapManager the map manager
     * @param latLng the coordinates of the event
     * @param eventName the name of the event
     * @param zoomLevel the zoom level of the map
     */
    public void setEventOnMap(MapManager mapManager, LatLng latLng, String eventName, float zoomLevel) {
        mapManager.clear();
        mapManager.addMarker(eventName, latLng);
        mapManager.moveCamera(latLng, zoomLevel);
    }
}
