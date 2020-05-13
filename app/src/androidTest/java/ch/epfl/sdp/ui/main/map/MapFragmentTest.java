package ch.epfl.sdp.ui.main.map;

import android.Manifest;
import android.location.Location;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiSelector;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.LocationQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.mocks.MockLocationService;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static junit.framework.TestCase.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapFragmentTest {
    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");
    private static final String DUMMY_EVENTREF1 = "sdkljfgh34phrt";
    private static final String DUMMY_EVENTREF2 = "sdkelrituhfgh34phrt";
    private static final double FAKE_GPS_LATITUDE = 46.5296363;
    private static final double FAKE_GPS_LONGITUDE = 6.561525199999999;
    private static final LatLng POSITION_1 = new LatLng(46.5296363, 6.561525199999999);
    private static final LatLng POSITION_2 = new LatLng(46.518003199999995, 6.5922564);

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mAttendingEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mOwnedEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<Collection<DatabaseObject<Event>>> mLocationEventsLiveData = new MutableLiveData<>();

    private EventBuilder eventBuilder = new EventBuilder();
    private Event eventTest1 = eventBuilder.setTitle("event1").setDescription("description").setDate("02/01/2100 00:00").
            setOrganizerRef("organizer1").setLocation(POSITION_1).build();
    private Event eventTest2 = eventBuilder.setTitle("event2").setDescription("description2").setDate("02/01/2100 00:00").
            setOrganizerRef("organizer2").setLocation(POSITION_2).build();
    private FragmentScenario mScenario;
    private Location mLocation = new Location("Default");
    UiDevice mUiDevice =  UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Mock
    private Database mDatabase;

    @Mock
    private Authenticator mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Mock
    private FilterQuery mArrayFilterQuery;

    @Mock
    private FilterQuery mFieldFilterQuery;

    @Mock
    private LocationQuery mLocationQuery;

    @Mock
    private MockLocationService mMockLocationService;

    @Mock
    private MapManager mMapManagerMock;

    @Rule
    public GrantPermissionRule permissionFineRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        when(mCollectionQuery.document(DUMMY_USERREF)).thenReturn(mDocumentQuery);
        when(mCollectionQuery.whereArrayContains(anyString(), any())).thenReturn(mArrayFilterQuery);
        when(mCollectionQuery.whereFieldEqualTo(anyString(), any())).thenReturn(mFieldFilterQuery);
        when(mCollectionQuery.atLocation(any(), anyDouble())).thenReturn(mLocationQuery);
        when(mLocationQuery.liveData(Event.class)).thenReturn(mLocationEventsLiveData);
        when(mArrayFilterQuery.liveData(Event.class)).thenReturn(mAttendingEventsLiveData);
        when(mFieldFilterQuery.liveData(Event.class)).thenReturn(mOwnedEventsLiveData);
        when(mDocumentQuery.liveData(User.class)).thenReturn(mUserLiveData);
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mCollectionQuery.document(DUMMY_EVENTREF1)).thenReturn(mDocumentQuery);
        when(mCollectionQuery.document(DUMMY_EVENTREF2)).thenReturn(mDocumentQuery);
        doNothing().when(mDocumentQuery).update(anyString(), any(), any());

        mLocation.setLatitude(FAKE_GPS_LATITUDE);
        mLocation.setLongitude(FAKE_GPS_LONGITUDE);
        when(mMockLocationService.getLastKnownLocation(any())).thenReturn(mLocation);
    }

    private void scenario(){
        mScenario = FragmentScenario.launchInContainer(
                MapFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(MapFragment.class, mMapManagerMock, mMockLocationService, mDatabase, mAuthenticator)
        );

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mScenario.onFragment(fragment -> {mEventsLiveData.setValue(events);});
    }

    @Test
    public void MapFragment_CheckThatMapIsDisplayedWithMockDatabase() {
        scenario();
        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }

    @Test
    public void MapFragment_CheckThatTestMarkersAreDisplayedAndClickable() throws UiObjectNotFoundException {
        scenario();

        UiObject marker1 = mUiDevice.findObject(new UiSelector().descriptionContains("event1"));
        UiObject marker2 = mUiDevice.findObject(new UiSelector().descriptionContains("event2"));

        assertNotNull(marker1);
        assertNotNull(marker2);

        marker1.click();
        marker2.click();
    }
}