package ch.epfl.sdp.ui.main.map;

import android.location.Location;
import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

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
    private static final double DEGREE_IN_KM = 111.11;

    public MapViewModel(@NonNull Database database, @NonNull MapManager mapManager) {
        mDatabase = database;
        mCollectionQuery = database.query("events");
        mMapManager = mapManager;
    }

    public void initializeMapManagerWithLastKnowLocation(Location lastKnownLocation, LifecycleOwner lifecycleOwner) {
        this.moveCamera(lastKnownLocation, 12);
        this.setMyLocation();
        this.addMarkersNearLocation(lifecycleOwner, lastKnownLocation, 100);
    }

    public void initializeMapManagerWithoutLastKnowLocation(LifecycleOwner lifecycleOwner) {
        this.moveCameraDefault();
        this.addMarkers(lifecycleOwner);
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

    public void addMarkersNearLocation(LifecycleOwner lifecycleOwner, Location location, double distanceInKm) {
        getEvents().observe(lifecycleOwner, events -> {
            for(Event event: events){
                LatLng coordinates = event.getLocation();

                if (location != null) {
                    double lLat = location.getLatitude();
                    double lLong = location.getLongitude();
                    double eLat = coordinates.latitude;
                    double eLong = coordinates.longitude;
                    double diffLat = Math.abs(lLat - eLat);
                    double diffLong = Math.abs(lLong - eLong);
                    double LatKm = DEGREE_IN_KM * diffLat;
                    double LongKm = DEGREE_IN_KM * diffLong * Math.cos(diffLat);
                    if ((LatKm < distanceInKm) && (LongKm < distanceInKm)) {
                        String name = event.getTitle();
                        Marker marker = mMapManager.addMarker(new MarkerOptions().position(coordinates).title(name));
                    }
                }
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
