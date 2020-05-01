package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class DefaultEventFragmentTest {

    private final static String DUMMY_TITLE = "title";
    private final static String DUMMY_DESCRIPTION = "description";
    private final static String DUMMY_REF = "dfghkjh234l5";
    private final static EventBuilder sEventBuilder = new EventBuilder();
    private Event DUMMY_EVENT = sEventBuilder
            .setTitle(DUMMY_TITLE)
            .setDescription(DUMMY_DESCRIPTION)
            .setDate("01/01/2021")
            .setOrganizerRef(DUMMY_REF)
            .build();

    @Mock
    private Database mDatabaseMock;

    @Mock
    private CollectionQuery mCollectionQueryMock;

    @Mock
    private DocumentQuery mDocumentQueryMock;

    private MutableLiveData<Event> mEventsLive = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.livedata(Event.class)).thenReturn(mEventsLive);
        mEventsLive.postValue(DUMMY_EVENT);
    }

    @SuppressWarnings("unchecked")
    private void scenario(Bundle bundle) {
        FragmentScenario<DefaultEventFragment> scenario = FragmentScenario.launchInContainer(
                DefaultEventFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(DefaultEventFragment.class, mDatabaseMock, "anyRef")
        );

    }
    @SuppressWarnings("unchecked")
    @Test
    public void DefaultEventFragment_CalendarIntent() throws InterruptedException {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");

        scenario(bundle);
        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));

        Intents.init();

        Intent resultIntent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultIntent);
        intending(allOf(
                hasType("vnd.android.cursor.item/event"),
                hasExtra(CalendarContract.Events.TITLE, DUMMY_TITLE),
                hasExtra(CalendarContract.Events.EVENT_LOCATION, DUMMY_EVENT.getAddress()),
                hasExtra(CalendarContract.Events.DESCRIPTION, DUMMY_EVENT.getDescription())
        )).respondWith(result);
        onView(withId(R.id.calendar_Button)).perform(click());
        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));
        
        Intents.release();
        Thread.sleep(1000);
    }

    @Test
    public void DefaultEventFragment_EventIsLoaded() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.title)).check(matches(withText(containsString(DUMMY_TITLE))));
        onView(withId(R.id.description)).check(matches(withText(containsString(DUMMY_DESCRIPTION))));

        onView(withId(R.id.date)).check(matches(isDisplayed()));
        onView(withId(R.id.address)).check(matches(isDisplayed()));
        onView(withId(R.id.minimap)).check(matches(isDisplayed()));
    }

}
