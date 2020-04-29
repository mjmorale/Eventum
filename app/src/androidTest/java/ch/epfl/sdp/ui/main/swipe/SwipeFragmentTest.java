package ch.epfl.sdp.ui.main.swipe;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.rule.ActivityTestRule;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.main.MainActivity;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.swipeLeft;
import static androidx.test.espresso.action.ViewActions.swipeRight;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class SwipeFragmentTest {

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    private EventBuilder eventBuilder = new EventBuilder();
    private Event eventTest1 = eventBuilder.setTitle("title").setDescription("description").setDate("01/01/2020").setOrganizerRef("organizer1").build();
    private Event eventTest2 = eventBuilder.setTitle("title2").setDescription("description2").setDate("01/01/2020").setOrganizerRef("organizer2").build();

    @Rule
    public ActivityTestRule<MainActivity> mActivityRule = new ActivityTestRule<>(MainActivity.class);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    private void scenario(MutableLiveData<List<Event>> eventLiveData){
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(eventLiveData);

        FragmentScenario<SwipeFragment> scenario = FragmentScenario.launchInContainer(
                SwipeFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(SwipeFragment.class, mDatabase));
    }

    @Test
    public void SwipeFragment_NewCardIsShow() throws InterruptedException {
        MutableLiveData<List<Event>> eventLiveData = new MutableLiveData<>();

        scenario(eventLiveData);

        List<Event> events = new ArrayList<>();
        events.add(eventTest1);
        eventLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
    }

    @Test
    public void SwipeFragment_SwipeLeftAndRightRemoveCard() throws InterruptedException {
        MutableLiveData<List<Event>> eventLiveData = new MutableLiveData<>();

        scenario(eventLiveData);

        List<Event> events = new ArrayList<>();
        events.add(eventTest1);
        eventLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
        onView(withId(R.id.cards_list_view)).perform(swipeLeft());
        Thread.sleep(1500);
        onView(withText("title")).check(doesNotExist());
    }

    @Test
    public void SwipeFragment_SecondCardIsShowAfterSwipe() throws InterruptedException {
        MutableLiveData<List<Event>> eventLiveData = new MutableLiveData<>();

        scenario(eventLiveData);

        List<Event> events = new ArrayList<>();
        events.add(eventTest1);
        events.add(eventTest2);
        eventLiveData.postValue(events);

        Thread.sleep(1500);
        onView(withText("title")).check(matches(isDisplayed()));
        onView(withId(R.id.cards_list_view)).perform(swipeRight());
        Thread.sleep(1500);
        onView(withText("title")).check(doesNotExist());
        onView(withText("title2")).check(matches(isDisplayed()));
    }

    @Test
    public void SwipeFragment_ClickSToDetailled(){
        onView(withId(R.id.cards_list_view)).perform(click());
    }

}

