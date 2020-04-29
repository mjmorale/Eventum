package ch.epfl.sdp.ui.event;


import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.map.MapManager;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.containsString;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

// This is an example implementation of an instrumented fragment test.
@RunWith(MockitoJUnitRunner.class)
public class DefaultEventFragmentTest {

    @Mock
    private Database mDatabaseMock;

    @Mock
    private CollectionQuery mCollectionQueryMock;

    @Mock
    private DocumentQuery mDocumentQueryMock;

    @Mock
    private MapManager mMapManagerMock;

    private MutableLiveData<Event> mEventsLive = new MutableLiveData<>();

    private String title = "title";
    private String description = "description";
    private EventBuilder eventBuilder = new EventBuilder();
    private Event eventTest = eventBuilder.setTitle(title).setDescription(description).setDate("01/01/2021").build();

    @Before
    public void setup() { MockitoAnnotations.initMocks(this); }

    @SuppressWarnings("unchecked")
    private void scenario(Bundle bundle) {

        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.livedata(Event.class)).thenReturn(mEventsLive);

        FragmentScenario<DefaultEventFragment> scenario = FragmentScenario.launchInContainer(
                DefaultEventFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(DefaultEventFragment.class, mDatabaseMock, "anyRef")
        );
    }


    @Test
    public void DefaultEventFragment_EventIsLoaded() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");

        scenario(bundle);

        mEventsLive.postValue(eventTest);

        onView(withId(R.id.title)).check(matches(withText(containsString(title))));
        onView(withId(R.id.description)).check(matches(withText(containsString(description))));

        onView(withId(R.id.date)).check(matches(isDisplayed()));
        onView(withId(R.id.address)).check(matches(isDisplayed()));
        onView(withId(R.id.minimap)).check(matches(isDisplayed()));
    }



}
