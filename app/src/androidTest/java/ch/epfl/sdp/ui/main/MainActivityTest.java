package ch.epfl.sdp.ui.main;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.List;

import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.DataInteraction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.EventActivity;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    private static final String DUMMY_EVENTREF = "saielrkfuth2n340i7fz";
    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final User DUMMY_USER = new User("testname", "testemail");

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();
    private MutableLiveData<List<Event>> mEventsLiveData = new MutableLiveData<>();

    private UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public ActivityTestRule<MainActivity> mActivity = new ActivityTestRule<>(MainActivity.class, false, false);

    @Rule public GrantPermissionRule mPermissionFine = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_FINE_LOCATION);
    @Rule public GrantPermissionRule mPermissionCoarse = GrantPermissionRule.grant(android.Manifest.permission.ACCESS_COARSE_LOCATION);

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MainActivity_UserData_DisplayCorrectUserData() throws Throwable {
        launchDefaultActivity(DUMMY_USERREF);

        mActivity.runOnUiThread(() -> {
            mUserLiveData.setValue(DUMMY_USER);
        });

        onView(withId(R.id.main_nav_header_username)).check(matches(withText(containsString(DUMMY_USER.getName()))));
        onView(withId(R.id.main_nav_header_email)).check(matches(withText(containsString(DUMMY_USER.getEmail()))));
    }

    @Test
    public void MainActivity_CreateEvent_LaunchesEventActivityOnSuccess() {
        launchDefaultActivity(DUMMY_USERREF);

        Intents.init();

        Intent resultIntent = new Intent();
        resultIntent.putExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ORGANIZER);
        resultIntent.putExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_EVENTREF);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultIntent);

        intending(hasComponent("ch.epfl.sdp.ui.createevent.CreateEventActivity")).respondWith(result);

        onView(withId(R.id.main_actionbar_add)).perform(click());

        onView(withId(R.id.default_event_layout)).check(matches(isDisplayed()));

        Intents.release();
    }

    @Test
    public void MainActivity_CreateEvent_DisplaysAToastOnFailure() {
        launchDefaultActivity(DUMMY_USERREF);

        Intents.init();

        Intent resultIntent = new Intent();
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_CANCELED, resultIntent);

        intending(hasComponent("ch.epfl.sdp.ui.createevent.CreateEventActivity")).respondWith(result);

        onView(withId(R.id.main_actionbar_add)).perform(click());

        onView(withText("Failed to create event")).inRoot(withDecorView(not(mActivity.getActivity().getWindow().getDecorView()))).check(matches(isDisplayed()));

        Intents.release();
    }

    @Test
    public void MainActivity_Settings_CanBeOpenedFromNavigationView() {
        launchDefaultActivity(DUMMY_USERREF);

        Intents.init();

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_settings));

        intended(hasComponent("ch.epfl.sdp.ui.settings.SettingsActivity"));

        Intents.release();
    }

    @Test
    public void MainActivity_UserProfile_CanBeOpenedFromNavigationView() {
        launchDefaultActivity(DUMMY_USERREF);

        Intents.init();

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_header_layout))
                .perform(click());

        intended(allOf(hasComponent("ch.epfl.sdp.ui.user.UserActivity"), hasExtra(UIConstants.BUNDLE_USER_REF, DUMMY_USERREF)));

        Intents.release();
    }

    @Test
    public void MainActivity_Navigation_CanOpenFromNavigation() {
        launchDefaultActivity(DUMMY_USERREF);

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_map));

        onView(withId(R.id.mapView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void MainActivity_Navigation_CanOpenAttendingFromNavigation() {
        launchDefaultActivity(DUMMY_USERREF);

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_attending));

        onView(withId(R.id.attending_list_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void MainActivity_Navigation_CanOpenSwipeFromNavigation() {
        launchDefaultActivity(DUMMY_USERREF);

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_view))
                .perform(NavigationViewActions.navigateTo(R.id.nav_home));

        onView(withId(R.id.cards_list_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void MainActivity_Navigation_ClosesDrawerWhenPressingBackButton() throws InterruptedException {
        launchDefaultActivity(DUMMY_USERREF);

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        mDevice.pressBack();

        Thread.sleep(1000);

        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)));
    }

    private void launchDefaultActivity(String userRef) {
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.liveData(Event.class)).thenReturn(mEventsLiveData);
        when(mCollectionQuery.document(userRef)).thenReturn(mDocumentQuery);
        when(mDocumentQuery.livedata(User.class)).thenReturn(mUserLiveData);
        ServiceProvider.getInstance().setDatabase(mDatabase);

        Intent intent = new Intent();
        intent.putExtra(UIConstants.BUNDLE_USER_REF, userRef);
        mActivity.launchActivity(intent);
    }
}
