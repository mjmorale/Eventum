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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
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
    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final String DUMMY_EVENTREF = "kdjhfgslkjehrg";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");

    @Mock
    private Database mDatabase;

    @Mock
    private Authenticator mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private FilterQuery mFilterQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void AttendingListFragment_DisplayGivenListOfEvents() {
        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = startScenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>("asdfasdfasdf", DUMMY_EVENT));
        eventLiveData.postValue(events);

        onView(withText("testtitle")).check(matches(isDisplayed()));
    }

    @Test
    public void AttendingListFragment_ClickOnItemStartsEventActivity() {
        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = startScenario();

        List<DatabaseObject<Event>> events = new ArrayList<>();
        events.add(new DatabaseObject<>(DUMMY_EVENTREF, DUMMY_EVENT));
        eventLiveData.postValue(events);

        Intents.init();

        intending(hasComponent("ch.epfl.sdp.ui.event.EventActivity"))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(withText("testtitle")).perform(click());

        intended(allOf(
                hasComponent("ch.epfl.sdp.ui.event.EventActivity"),
                hasExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_EVENTREF)));

        Intents.release();
    }

    private MutableLiveData<List<DatabaseObject<Event>>> startScenario() {
        MutableLiveData<List<DatabaseObject<Event>>> eventLiveData = new MutableLiveData<>();

        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.whereArrayContains(anyString(), any())).thenReturn(mFilterQuery);
        when(mFilterQuery.liveData(Event.class)).thenReturn(eventLiveData);

        FragmentScenario.launchInContainer(
                AttendingListFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(AttendingListFragment.class, mAuthenticator, mDatabase));

        return eventLiveData;
    }
}
