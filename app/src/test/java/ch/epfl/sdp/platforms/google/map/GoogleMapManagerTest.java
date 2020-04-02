package ch.epfl.sdp.platforms.google.map;

import android.location.Location;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoogleMapManagerTest {

    @Mock
    private GoogleMap mGoogleMap;

    @Mock
    private Marker mMarker;

    @Mock
    MarkerOptions mMarkerOptions;

    @Mock
    private LatLng mLatLng;

    @Mock
    private Location mLocation;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void GoogleMapManager_CheckAddMarkerWithMarkerOptions() {
        when(mGoogleMap.addMarker(mMarkerOptions)).thenReturn(mMarker);
        GoogleMapManager mapManager = new GoogleMapManager(mGoogleMap);
        mapManager.addMarker(mMarkerOptions);
        verify(mGoogleMap).addMarker(mMarkerOptions);
    }

    @Test
    public void GoogleMapManager_CheckAddMarkerWithLocation() {
        String locationName = "location name";
        when(mGoogleMap.addMarker(any())).thenReturn(mMarker);
        GoogleMapManager mapManager = new GoogleMapManager(mGoogleMap);
        mapManager.addMarker(locationName, mLatLng);
        verify(mGoogleMap).addMarker(any());
    }

//    @Test
//    public void checkThatMoveCameraUseMoveCameraOnTheMap() {
//        float zoomLevel = 4;
//        doNothing().when(mGooogleMap).moveCamera(any());
//        GoogleMapManager mapManager = new GoogleMapManager(mGooogleMap);
//        mapManager.moveCamera(mLocation, zoomLevel);
//        verify(mGooogleMap).moveCamera(any());
//    }

    @Test
    public void GoogleMapManager_CheckThatSetMyLocationSetLocationEnabledWithTrueOnTheMap() {
        GoogleMapManager mapManager = new GoogleMapManager(mGoogleMap);
        mapManager.setMyLocation();
        verify(mGoogleMap).setMyLocationEnabled(true);
    }
}
