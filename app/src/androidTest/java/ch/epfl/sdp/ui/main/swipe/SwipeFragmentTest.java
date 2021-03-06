package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.uiautomator.UiDevice;

import com.google.android.gms.maps.model.LatLng;

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
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.mocks.MockLocationService;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withSubstring;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SwipeFragmentTest {

    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");
    private static final String DUMMY_EVENTREF1 = "sdkljfgh34phrt";
    private static final String DUMMY_EVENTREF2 = "sdkelrituhfgh34phrt";
    private static final String DUMMY_DISTANCE_EVENT1 = "201 m";
    private static final String DUMMY_DISTANCE_EVENT2 = "3 km";

    private FragmentScenario<SwipeFragment> mScenario;

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

    private EventBuilder eventBuilder = new EventBuilder();

    private Event eventTest1 = eventBuilder
            .setTitle("title")
            .setDescription("description")
            .setDate("01/01/2100 00:00")
            .setLocation(new LatLng(46.518748, 6.567795))
            .setOrganizerRef("organizer1")
            .build();
    private Event eventTest2 = eventBuilder
            .setTitle("title2")
            .setDescription("description2")
            .setDate("02/01/2100 00:00")
            .setLocation(new LatLng(46.541122, 6.601410))
            .setOrganizerRef("organizer2")
            .build();

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mAttendingEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mOwnedEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<Collection<DatabaseObject<Event>>> mLocationEventsLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        when(mCollectionQuery.document(DUMMY_USERREF)).thenReturn(mDocumentQuery);
        when(mCollectionQuery.document(DUMMY_EVENTREF1)).thenReturn(mDocumentQuery);
        when(mCollectionQuery.document(DUMMY_EVENTREF2)).thenReturn(mDocumentQuery);
        when(mCollectionQuery.whereArrayContains(anyString(), any())).thenReturn(mArrayFilterQuery);
        when(mCollectionQuery.whereFieldEqualTo(anyString(), any())).thenReturn(mFieldFilterQuery);
        when(mCollectionQuery.atLocation(any(), anyDouble())).thenReturn(mLocationQuery);
        when(mLocationQuery.liveData(Event.class)).thenReturn(mLocationEventsLiveData);
        when(mArrayFilterQuery.liveData(Event.class)).thenReturn(mAttendingEventsLiveData);
        when(mFieldFilterQuery.liveData(Event.class)).thenReturn(mOwnedEventsLiveData);
        when(mDocumentQuery.liveData(User.class)).thenReturn(mUserLiveData);
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        doNothing().when(mDocumentQuery).update(anyString(), any(), any());
    }

    private void scenario() {
        mScenario = FragmentScenario.launchInContainer(
                SwipeFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(SwipeFragment.class, mDatabase, mAuthenticator, new MockLocationService()));
    }

    private void scenarioWithBundle() {
        Bundle bundle = new Bundle();
        bundle.putInt("eventHash", eventTest2.hashCode());

        mScenario = FragmentScenario.launchInContainer(
                SwipeFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(SwipeFragment.class, mDatabase, mAuthenticator, new MockLocationService()));
    }

    @Test
    public void SwipeFragment_NewCardIsShow() throws InterruptedException {
        scenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        mEventsLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
    }

    @Test
    public void SwipeFragment_DistanceIsShownInMeters() throws InterruptedException {
        scenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        mEventsLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withId(R.id.eventDistance)).check(matches(withSubstring(DUMMY_DISTANCE_EVENT1)));
    }

    @Test
    public void SwipeFragment_DistanceIsShownInKilometers() throws InterruptedException {
        scenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mEventsLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withId(R.id.eventDistance)).check(matches(withSubstring(DUMMY_DISTANCE_EVENT2)));
    }

    @Test
    public void SwipeFragment_SwipeLeftAndRightRemoveCard() throws InterruptedException {
        scenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        mEventsLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
        onView(withId(R.id.cards_list_view)).perform(swipeLeft());
        Thread.sleep(1500);
        onView(withText("title")).check(doesNotExist());
    }

    @Test
    public void SwipeFragment_SecondCardIsShowAfterSwipe() throws InterruptedException {
        scenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mScenario.onFragment(fragment -> {mEventsLiveData.setValue(events);});

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
        onView(withId(R.id.cards_list_view)).perform(swipeRight());

        Thread.sleep(1500);
        onView(withText("title")).check(doesNotExist());
        onView(withText("title2")).check(matches(isDisplayed()));
    }

    @Test
    public void SwipeFragment_ClickSToDetailed() {
        scenario();
        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mScenario.onFragment(fragment -> mEventsLiveData.setValue(events));

        onView(withId(R.id.cards_list_view)).perform(click());
        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));
        onView(allOf(withText(eventTest1.getTitle()), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withText(eventTest1.getDescription()), isDisplayed())).check(matches(isDisplayed()));
    }

    @Test
    public void SwipeFragment_FromTheMapWithBundleShowTheRightCard() throws InterruptedException {
        scenarioWithBundle();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mScenario.onFragment(fragment -> {mEventsLiveData.setValue(events);});

        Thread.sleep(1500);

        onView(withText("title2")).check(matches(isDisplayed()));
        onView(withId(R.id.cards_list_view)).perform(click());
        onView(allOf(withText(eventTest2.getTitle()), isDisplayed())).check(matches(isDisplayed()));
        onView(allOf(withText(eventTest2.getDescription()), isDisplayed())).check(matches(isDisplayed()));
    }

    @Test
    public void SwipeFragment_FromTheMapWithBundleBackToMapAfterSwipe() throws InterruptedException {
        scenarioWithBundle();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mScenario.onFragment(fragment -> {mEventsLiveData.setValue(events);});

        Thread.sleep(1500);

        onView(withId(R.id.cards_list_view)).perform(swipeLeft());

        Thread.sleep(1500);

        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }

    @Test
    public void SwipeFragment_FromTheMapWithBundleBackToMapAfterBack() throws InterruptedException {
        scenarioWithBundle();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF2, eventTest2));
        mScenario.onFragment(fragment -> {mEventsLiveData.setValue(events);});

        Thread.sleep(1500);

        UiDevice.getInstance(InstrumentationRegistry.getInstrumentation()).pressBack();

        Thread.sleep(1500);

        onView(withId(R.id.mapView)).check(matches((isDisplayed())));
    }

    @Test
    public void SwipeFragment_ReloadButtonWorks() throws InterruptedException {
        scenario();
        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF1, eventTest1));
        mScenario.onFragment(fragment -> mEventsLiveData.setValue(events));

        Thread.sleep(1500);
        onView(withId(R.id.cards_list_view)).perform(swipeRight());

        Thread.sleep(1500);
        onView(withId(R.id.reload_swipe_button)).perform(click());

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
    }
}

