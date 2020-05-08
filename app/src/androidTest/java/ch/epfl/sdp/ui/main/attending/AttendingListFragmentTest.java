package ch.epfl.sdp.ui.main.attending;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.offline.EventSaver;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class AttendingListFragmentTest {

    private static final EventBuilder sEventBuilder = new EventBuilder();
    private static final Event DUMMY_EVENT = sEventBuilder
            .setTitle("testtitle")
            .setDescription("description")
            .setDate("01/01/2020")
            .setOrganizerRef("testref")
            .setAddress("testaddress")
            .setImageId("testid")
            .setLocation(new LatLng(12.3, 45.6))
            .build();
    private static final Event DUMMY_EVENT2 = sEventBuilder
            .setTitle("testtitle2")
            .setDescription("description2")
            .setDate("01/01/2020")
            .setOrganizerRef("testref2")
            .setAddress("testaddress2")
            .setImageId("testid2")
            .setLocation(new LatLng(12.3, 45.6))
            .build();
    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final String DUMMY_EVENTREF = "kdjhfgslkjehrg";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");
    private static final File DUMMY_DIR = new File("dummypath");

    @Mock
    private Database mDatabase;

    @Mock
    private Authenticator mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private FilterQuery mFilterQuery;

    @Mock
    private EventSaver mEventSaver;

    private FragmentScenario<AttendingListFragment> mScenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        mScenario = null;
    }

    @SuppressWarnings("unchecked")
    @Test
    public void AttendingListFragment_DisplayGivenIfOffline() throws IOException, ClassNotFoundException {
        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>("asdfasdfasdf", DUMMY_EVENT));

        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = startScenario(events);

        eventLiveData.postValue(null);

        onView(withText("testtitle")).check(matches(isDisplayed()));
    }

    @Test
    public void AttendingListFragment_ClickOnItemStartsEventActivity() throws IOException, ClassNotFoundException {
        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF, DUMMY_EVENT));

        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = startScenario(events);

        eventLiveData.postValue(null);

        Intents.init();

        intending(hasComponent("ch.epfl.sdp.ui.event.EventActivity"))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(withText("testtitle")).perform(click());

        intended(allOf(
                hasComponent("ch.epfl.sdp.ui.event.EventActivity"),
                hasExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_EVENTREF)));

        Intents.release();
    }

    @Test
    public void AttendingListFragment_UpdatesCacheIfOnline() throws IOException, ClassNotFoundException {
        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF, DUMMY_EVENT));

        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = startScenario(events);

        List<DatabaseObject<Event>> events_updated = new ArrayList<>();
        events_updated.add(new DatabaseObject<>(DUMMY_EVENTREF, DUMMY_EVENT2));

        mScenario.onFragment(fragment -> {
            eventLiveData.setValue(events_updated);
        });

        verify(mEventSaver).saveEvent(DUMMY_EVENT2, DUMMY_EVENTREF, DUMMY_EVENT2.getDate(), DUMMY_DIR);
    }

    private MutableLiveData<List<DatabaseObject<Event>>> startScenario(List<DatabaseObject<Event>> cacheContent) throws IOException, ClassNotFoundException {
        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = new MutableLiveData<>();

        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.whereArrayContains(anyString(), any())).thenReturn(mFilterQuery);
        when(mFilterQuery.liveData(Event.class)).thenReturn(eventLiveData);
        when(mEventSaver.getAllEventsWithRefs(DUMMY_DIR)).thenReturn(cacheContent);
        doNothing().when(mEventSaver).saveEvent(any(), anyString(), any(), any());

        mScenario = FragmentScenario.launchInContainer(
                AttendingListFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(AttendingListFragment.class, mAuthenticator, mEventSaver, DUMMY_DIR, mDatabase));

        return eventLiveData;
    }
}
