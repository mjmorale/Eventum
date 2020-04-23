package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.Marker;

import java.util.Collections;
import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class MapViewModel extends ViewModel {

    static class MapViewModelFactory extends ParameterizedViewModelFactory {
        MapViewModelFactory() {
            super(MapManager.class, LocationService.class);
        }

        void setMapManager(@NonNull MapManager mapManager ){
            setValue(0,verifyNotNull(mapManager));
        }

        void setLocationService(@NonNull LocationService locationService ){
            setValue(1,verifyNotNull(locationService));
        }
    }
    private MapManager<Marker> mMapManager;
    private LocationService mLocationService;
    private Dictionary<Marker, Event> mEventsMarkers = new Hashtable<>();

    public MapViewModel(@NonNull MapManager<Marker> mapManager,  @NonNull LocationService locationService) {
        mMapManager = mapManager;
        mLocationService = locationService;
    }

    public void centerCamera(Context context, float zoomLevel) {
        Location location = mLocationService.getLastKnownLocation(context);
        mMapManager.moveCamera(location, zoomLevel);
    }

    public void addEvent(Event event) {
        mEventsMarkers.put(mMapManager.addMarker(event.getTitle(), event.getLocation()), event);
    }

    public void clearEvents() {
        List<Marker> markers = Collections.list(mEventsMarkers.keys());
        for (Marker marker : markers )
            marker.remove();
        mEventsMarkers = new Hashtable<>();
    }

    public Event getEventFromMarker(Marker marker) {
        return mEventsMarkers.get(marker);
    }

}
