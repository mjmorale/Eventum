package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import com.google.firebase.auth.AuthCredential;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.intent.Intents;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.mocks.MockWeatherFetcher;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.weather.WeatherFetcher;

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

@RunWith(MockitoJUnitRunner.class)
public class EventFragmentTest {

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

    @Mock
    private FilterQuery mFilterQueryMock;

    @Mock
    private Authenticator<AuthCredential> mAuthenticatorMock;


    private WeatherFetcher mWeatherFetcherMock = new MockWeatherFetcher();

    private MutableLiveData<Event> mEventsLive = new MutableLiveData<>();

    LiveData<List<DatabaseObject<ChatMessage>>> mChatLiveData = new MutableLiveData<>();

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.liveData(Event.class)).thenReturn(mEventsLive);
        mEventsLive.postValue(DUMMY_EVENT);
    }

    @SuppressWarnings("unchecked")
    private void scenario(Bundle bundle) {
        FragmentScenario<EventFragment> scenario = FragmentScenario.launchInContainer(
                EventFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(EventFragment.class, mDatabaseMock, "anyRef", mWeatherFetcherMock)
        );

    }
    @SuppressWarnings("unchecked")
    @Test
    public void EventFragment_CalendarIntent() throws InterruptedException {
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
                hasExtra(CalendarContract.Events.DESCRIPTION, DUMMY_EVENT.getDescription()),
                hasExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, DUMMY_EVENT.getDate().getTime())
        )).respondWith(result);
        onView(withId(R.id.event_detail_calendar_button)).perform(click());
        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));
        
        Intents.release();
        Thread.sleep(1000);
    }

    @Test
    public void EventFragment_EventIsLoaded() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.title)).check(matches(withText(containsString(DUMMY_TITLE))));
        onView(withId(R.id.description)).check(matches(withText(containsString(DUMMY_DESCRIPTION))));

        onView(withId(R.id.date)).check(matches(isDisplayed()));
        onView(withId(R.id.address)).check(matches(isDisplayed()));
        onView(withId(R.id.minimap)).check(matches(isDisplayed()));
    }

    @Test
    public void EventFragment_LaunchesChatWithCorrectValues() {
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.collection(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.orderBy(anyString())).thenReturn(mFilterQueryMock);
        when(mFilterQueryMock.liveData(ChatMessage.class)).thenReturn(mChatLiveData);
        when(mAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo("testuid", "testname", "testemail"));
        ServiceProvider.getInstance().setDatabase(mDatabaseMock);
        ServiceProvider.getInstance().setAuthenticator(mAuthenticatorMock);

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        scenario(bundle);

        onView(withId(R.id.event_detail_chat_button)).perform(click());

        onView(withId(R.id.button_chatbox_send)).check(matches(isDisplayed()));
    }

}
