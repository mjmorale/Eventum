package ch.epfl.sdp.ui.main.map;

import android.Manifest;
import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Collection;
import java.util.List;
import ch.epfl.sdp.Event;
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
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MapFragmentTest {

    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");

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

    private MutableLiveData<List<DatabaseObject<Event>>> mEventsLive = new MutableLiveData<>();

    private MockLocationService mMockLocationService = new MockLocationService();

    @Mock
    private MapManager mMapManagerMock;

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mAttendingEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mOwnedEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<Collection<DatabaseObject<Event>>> mLocationEventsLiveData = new MutableLiveData<>();

    @Rule
    public GrantPermissionRule permissionFineRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void MapFragment_CheckThatMapIsDisplayedWithMockDatabase() {
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

        FragmentScenario.launchInContainer(
                MapFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(MapFragment.class, mMapManagerMock, mMockLocationService, mDatabase, mAuthenticator)
        );

        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }
}

