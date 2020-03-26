package ch.epfl.sdp.map;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import org.junit.Test;
import java.util.ArrayList;
import java.util.List;
import ch.epfl.sdp.ui.map.GoogleMapProvider;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.atLeast;
import static org.mockito.Mockito.atMost;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class MapUnitTest {
    private GoogleMap mGoogleMap = mock(GoogleMap.class);
    List<MarkerOptions> mMarkerOptions = new ArrayList<>();

    @Test
    public void setMapSettingsWithoutPermissionSetMyLocationEnabledWithFalse() {
        GoogleMapProvider.setMapSettings(mGoogleMap, mMarkerOptions, false);
        verify(mGoogleMap).setMyLocationEnabled(false);
    }

    @Test
    public void setMapSettingsWithPermissionSetMyLocationEnabledWithTrue() {
        GoogleMapProvider.setMapSettings(mGoogleMap, mMarkerOptions, true);
        verify(mGoogleMap).setMyLocationEnabled(true);
    }

    @Test
    public void setMapSettingsWith2MarkerOptionsListAdd2Marker() {
        mMarkerOptions.add(new MarkerOptions());
        mMarkerOptions.add(new MarkerOptions());
        GoogleMapProvider.setMapSettings(mGoogleMap, mMarkerOptions, true);
        verify(mGoogleMap, atLeast(2)).addMarker(any(MarkerOptions.class));
        verify(mGoogleMap, atMost(2)).addMarker(any(MarkerOptions.class));
    }
}