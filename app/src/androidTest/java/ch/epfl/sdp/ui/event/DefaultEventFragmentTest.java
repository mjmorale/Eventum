package ch.epfl.sdp.ui.event;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.provider.CalendarContract;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.test.espresso.intent.Intents;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.chat.ChatFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.anyIntent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

public class DefaultEventFragmentTest {

    private String mTitle= "EventTitle";
    private String mDescription ="EventDesc";
    private Date mDate = new Date();
    private Event mEvent= new EventBuilder().setTitle(mTitle).setDescription(mDescription).setDate(mDate).build();

    private MutableLiveData<Event> mEventLiveData = new MutableLiveData<>();

    @Mock
    private Database mDatabaseMock;
    @Mock
    private CollectionQuery mCollectionQueryMock;
    @Mock
    private DocumentQuery mDocumentQueryMock;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.livedata(Event.class)).thenReturn(mEventLiveData);
        mEventLiveData.postValue(mEvent);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void calendar_test() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        FragmentScenario<DefaultEventFragment> scenarioCalendar = FragmentScenario.launchInContainer(
                DefaultEventFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(DefaultEventFragment.class, mDatabaseMock, "anyRef")
        );

        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));


        Intents.init();
        Intent resultIntent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultIntent);
        intending(allOf(
                hasType("vnd.android.cursor.item/event"),
                hasExtra(CalendarContract.Events.TITLE, mTitle),
                hasExtra(CalendarContract.Events.EVENT_LOCATION,mEvent.getAddress()),
                hasExtra(CalendarContract.Events.DESCRIPTION, mEvent.getDescription())
        )
    ).respondWith(result);
        onView(withId(R.id.calendarButton)).perform(click());

        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));

        Intents.release();

    }

}
