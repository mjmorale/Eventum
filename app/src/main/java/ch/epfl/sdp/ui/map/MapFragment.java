package ch.epfl.sdp.ui.map;


import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import java.util.List;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;

public class MapFragment extends Fragment{
    private MapView mMapView;
    private GoogleMapProvider mGoogleMapProvider;
    private Database mDataBase;
    private LiveData<List<Event>> mEvents;

    public MapFragment(Database db) {
        mDataBase = db;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mEvents = mDataBase.query("events").liveData(Event.class);
    }

    
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        View view = inflater.inflate(R.layout.map_fragment, container, false);

        mMapView= view.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mGoogleMapProvider = new GoogleMapProvider(this ,mMapView);
        mGoogleMapProvider.setMyLocationEnabled(true);
        
        mEvents.observe(getViewLifecycleOwner(), event -> {
            for(Event e: event){
                addMarker(e.getTitle(),e.getLocation(), mGoogleMapProvider);
            }
        });

        return view;
    }

    public void addMarker(String eventName, LatLng coordinates, GoogleMapProvider googleMapProvider) {
        googleMapProvider.addMarker(new MarkerOptions().position(coordinates).title(eventName));
    }

    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }
}