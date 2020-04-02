package ch.epfl.sdp.platforms.google.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GoogleMapManagerTest {

    @Mock
    private GoogleMap mGooogleMap;

    @Mock
    private Marker mMarker;

    @Mock
    MarkerOptions mMarkerOptions;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void checkTAddMarker() {
        when(mGooogleMap.addMarker(mMarkerOptions)).thenReturn(mMarker);
        GoogleMapManager mapManager = new GoogleMapManager(mGooogleMap);
        mapManager.addMarker(mMarkerOptions);
        verify(mGooogleMap).addMarker(mMarkerOptions);
    }

    // to continue ................





}
