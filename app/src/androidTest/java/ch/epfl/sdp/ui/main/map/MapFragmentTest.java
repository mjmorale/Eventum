package ch.epfl.sdp.ui.main.map;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.GrantPermissionRule;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.mocks.MockLocationService;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapFragmentTest {

    @Rule public GrantPermissionRule mPermissionFine = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule public GrantPermissionRule mPermissionCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Mock
    private Database mDatabaseMock;

    @Mock
    private CollectionQuery mCollectionQuery;

    private MutableLiveData<List<Event>> mEventsLive = new MutableLiveData<>();

    @Mock
    private MapManager mMapManagerMock;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void MapFragment_CheckThatMapIsDisplayedWithMockDatabase() {
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLive);

        FragmentScenario<MapFragment> scenario = FragmentScenario.launchInContainer(
                MapFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(MapFragment.class, mMapManagerMock)
        );

        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }
}

