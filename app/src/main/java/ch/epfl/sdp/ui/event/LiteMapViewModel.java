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

    public static class LiteMapViewModelFactory extends ParameterizedViewModelFactory {

        public LiteMapViewModelFactory() {
            super(MapManager.class);
        }

        public void setMapManager(@NonNull MapManager mapManager) {
            setValue(0, verifyNotNull(mapManager));
        }
    }

    private MapManager mMapManager;

    /**
     * Construct a new LiteMapViewModel. This is for testing purposes, please use
     * the factory instead.
     *
     * @param mapManager The map manager service to use
     */
    public LiteMapViewModel(@NonNull MapManager mapManager) {
        mMapManager = mapManager;
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
