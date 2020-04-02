package ch.epfl.sdp.ui.main.map;

import android.location.Location;
import com.google.android.gms.maps.model.Marker;
import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.map.MapManager;
import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapViewModelTest {

    @Mock
    private Database mDatabase;

    @Mock
    private MapManager mMapManager;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private Marker mMarker;

    @Mock
    private Event mEvent;

    @Mock
    private Location mLocation;

    @Mock
    private LiveData<List<Event>> mEventsLiveData;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MapViewModel_MapViewModelConstructorDoTheRightQuery() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        doNothing().when(mEventsLiveData).observeForever(any());

        MapViewModel viewModel = new MapViewModel(mDatabase, mMapManager);
        verify(mDatabase).query("events");
    }

    @Test
    public void MapViewModel_MoveCameraMoveTheCameraOnTheMapManagerWithTheRightParameters() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        doNothing().when(mEventsLiveData).observeForever(any());

        float zoomLevel = 4;
        MapViewModel viewModel = new MapViewModel(mDatabase, mMapManager);
        viewModel.moveCamera(mLocation, zoomLevel);
        verify(mMapManager).moveCamera(mLocation, zoomLevel);
    }

    @Test
    public void MapViewModel_AddAndGetAnEventFromTheDictionaryReturnTheRightEvent() {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        doNothing().when(mEventsLiveData).observeForever(any());

        MapViewModel viewModel = new MapViewModel(mDatabase, mMapManager);
        viewModel.addEvent(mMarker, mEvent);
        Event event = viewModel.getEventFromMarker(mMarker);
        assertEquals(event, mEvent);
    }
}
