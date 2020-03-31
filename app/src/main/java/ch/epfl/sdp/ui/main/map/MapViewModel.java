package ch.epfl.sdp.ui.main.map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

// This is an example of a minimal implementation of a ViewModel.
public class MapViewModel extends ViewModel {

    static class MapViewModelFactory extends ParameterizedViewModelFactory {

        MapViewModelFactory() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

    private LiveData<List<Event>> mEventsLive;
    private final Database mDatabase;
    private GoogleMap mMap;
    private LiveData<GoogleMap> mMapLive;

    // This constructor can be used altought it should not be used in the actual application.
    // Use a view model factory instead.
    //TODO: replace with your own signature.
    public MapViewModel(@NonNull Database database) {
        mDatabase = database;
        mEventsLive = mDatabase.query("events").liveData(Event.class);
    }

    public LiveData<List<Event>> getEvents() {
        return mEventsLive;
    }

    public void addMarker(Event event) {
        if (mMap!=null) {
            LatLng coordinates = event.getLocation();
            String name = event.getTitle();
            Marker marker = mMap.addMarker(new MarkerOptions().position(coordinates).title(name));
        }
    }

    public GoogleMap getMap() {
        return mMap;
    }

    public void setMap(GoogleMap map) {
        mMap = map;
    }

}
