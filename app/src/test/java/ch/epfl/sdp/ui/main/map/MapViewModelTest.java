package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.location.Location;
import com.google.android.gms.maps.model.Marker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.map.MapManager;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapViewModelTest {

    @Mock
    private MapManager mMapManager;

    @Mock
    private Marker mMarker;

    @Mock
    private Event mEvent;

    @Mock
    private Location mLocation;

    @Mock
    private Context mContext;

    @Mock
    private LocationService mLocationService;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MapViewModel_MoveCameraMoveTheCameraOnTheMapManagerWithTheRightParameters() {
        when(mLocationService.getLastKnownLocation(any())).thenReturn(mLocation);

        float zoomLevel = 4;
        MapViewModel viewModel = new MapViewModel(mMapManager, mLocationService);
        viewModel.centerCamera(mContext, zoomLevel);
        verify(mMapManager).moveCamera(mLocation, zoomLevel);
    }
}
