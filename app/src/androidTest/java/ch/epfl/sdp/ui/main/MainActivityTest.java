package ch.epfl.sdp.ui.main;

import android.Manifest;
import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.view.Gravity;
import android.view.View;
import android.widget.SeekBar;

import com.google.android.gms.maps.model.LatLng;

import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import org.hamcrest.Matcher;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.EventCategory;
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
import ch.epfl.sdp.ui.ServiceProvider;
import ch.epfl.sdp.ui.UIConstants;
import ch.epfl.sdp.ui.event.EventActivity;

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
import static org.mockito.ArgumentMatchers.anyDouble;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class MainActivityTest {

    private static final EventBuilder sEventBuilder = new EventBuilder();
    private static final Event DUMMY_EVENT1 = sEventBuilder
            .setTitle("event1")
            .setDescription("description1")
            .setAddress("address1")
            .setDate(new Date())
            .setImageId("id1")
            .setOrganizerRef("ref1")
            .setLocation(new LatLng(12.3, 45.6))
            .build();
    private static final Event DUMMY_EVENT2 = sEventBuilder
            .setTitle("event2")
            .setDescription("description2")
            .setAddress("address2")
            .setDate(new Date())
            .setImageId("id2")
            .setOrganizerRef("ref2")
            .setLocation(new LatLng(12.3, 45.6))
            .build();
    private static final Event DUMMY_EVENT3 = sEventBuilder
            .setTitle("event3")
            .setDescription("description3")
            .setAddress("address3")
            .setDate(new Date())
            .setImageId("id3")
            .setOrganizerRef("ref3")
            .setLocation(new LatLng(12.3, 45.6))
            .build();
    private static final Event DUMMY_EVENT4 = sEventBuilder
            .setTitle("event4")
            .setDescription("description4")
            .setAddress("address4")
            .setDate(new Date("01/01/2100 00:00"))
            .setImageId("id4")
            .setOrganizerRef("ref4")
            .setLocation(new LatLng(12.3, 45.6))
            .build();
    private static final String DUMMY_EVENTREF = "saielrkfuth2n340i7fz";
    private static final String DUMMY_USERREF = "sdfkjghsdflkjghsdlfkgjh";
    private static final UserInfo DUMMY_USERINFO = new UserInfo(DUMMY_USERREF, "testname", "testemail");
    private static final User DUMMY_USER = new User("testname", "testemail");

    @Mock
    private Database mDatabase;

    @Mock
    private Authenticator mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private DocumentQuery mDocumentQuery;

    @Mock
    private LocationQuery mLocationQuery;

    @Mock
    private FilterQuery mArrayFilterQuery;

    @Mock
    private FilterQuery mFieldFilterQuery;

    private MutableLiveData<User> mUserLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mAttendingEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<List<DatabaseObject<Event>>> mOwnedEventsLiveData = new MutableLiveData<>();
    private MutableLiveData<Collection<DatabaseObject<Event>>> mLocationEventsLiveData = new MutableLiveData<>();

    private UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public ActivityTestRule<MainActivity> mActivity = new ActivityTestRule<>(MainActivity.class, false, false);

    @Rule
    public GrantPermissionRule permissionFineRule =
            GrantPermissionRule.grant(Manifest.permission.ACCESS_FINE_LOCATION);

    private static ViewAction setProgress(final int progress) {
        return new ViewAction() {
            @Override
            public void perform(UiController uiController, View view) {
                SeekBar seekBar = (SeekBar) view;
                seekBar.setProgress(progress);
            }
            @Override
            public String getDescription() {
                return "Set a progress on a SeekBar";
            }
            @Override
            public Matcher<View> getConstraints() {
                return ViewMatchers.isAssignableFrom(SeekBar.class);
            }
        };
    }

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

        intending(allOf(
                hasComponent("ch.epfl.sdp.ui.event.EventActivity"),
                hasExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_EVENTREF),
                hasExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ORGANIZER)))
                .respondWith(new Instrumentation.ActivityResult(Activity.RESULT_OK, new Intent()));

        onView(withId(R.id.main_actionbar_add)).perform(click());

        intended(allOf(
                hasComponent("ch.epfl.sdp.ui.event.EventActivity"),
                hasExtra(UIConstants.BUNDLE_EVENT_REF, DUMMY_EVENTREF),
                hasExtra(UIConstants.BUNDLE_EVENT_MODE_REF, EventActivity.EventActivityMode.ORGANIZER)));

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

        goToNavMenu(R.id.nav_map);

        onView(withId(R.id.mapView))
                .check(matches(isDisplayed()));
    }

    @Test
    public void MainActivity_Navigation_CanOpenAttendingFromNavigation() {
        launchDefaultActivity(DUMMY_USERREF);

        goToNavMenu(R.id.nav_attending);

        onView(withId(R.id.attending_list_view))
                .check(matches(isDisplayed()));
    }

    @Test
    public void MainActivity_Navigation_CanOpenSwipeFromNavigation() {
        launchDefaultActivity(DUMMY_USERREF);

        goToNavMenu(R.id.nav_home);

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
        when(mCollectionQuery.whereArrayContains(anyString(), any())).thenReturn(mArrayFilterQuery);
        when(mCollectionQuery.whereFieldEqualTo(anyString(), any())).thenReturn(mFieldFilterQuery);
        when(mCollectionQuery.atLocation(any(), anyDouble())).thenReturn(mLocationQuery);
        when(mLocationQuery.liveData(Event.class)).thenReturn(mLocationEventsLiveData);
        when(mArrayFilterQuery.liveData(Event.class)).thenReturn(mAttendingEventsLiveData);
        when(mFieldFilterQuery.liveData(Event.class)).thenReturn(mOwnedEventsLiveData);
        when(mDocumentQuery.liveData(User.class)).thenReturn(mUserLiveData);
        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        ServiceProvider.getInstance().setDatabase(mDatabase);
        ServiceProvider.getInstance().setAuthenticator(mAuthenticator);

        Intent intent = new Intent();
        intent.putExtra(UIConstants.BUNDLE_USER_REF, userRef);
        mActivity.launchActivity(intent);
    }

    private void goToNavMenu(int navId) {
        onView(withId(R.id.main_drawer_layout))
                .check(matches(isClosed(Gravity.LEFT)))
                .perform(DrawerActions.open());

        onView(withId(R.id.main_nav_view))
                .perform(NavigationViewActions.navigateTo(navId));
    }


    @Test
    public void MainActivity_FilterSettings_ShowCorrectValues() {
        launchDefaultActivity(DUMMY_USERREF);

        onView(withId(R.id.main_actionbar_search))
                .perform(click());

        // Check default progress is 5km
        onView(withId(R.id.seekBar_value))
                .check(matches(withText("5km")));

        onView(withId(R.id.optionIndoor))
                .perform(click());
        onView(withId(R.id.optionOutdoor))
                .perform(click());
        onView(withId(R.id.optionSport))
                .perform(click());
        onView(withId(R.id.optionParty))
                .perform(click());
        onView(withId(R.id.optionIndoor))
                .perform(click());

        onView(withId(R.id.seekBar_range))
                .perform(setProgress(9));

        onView(withId(R.id.seekBar_value))
                .check(matches(withText("10km")));
    }


    @Test
    public void MainActivity_FilterSettings_ResultLiveDataIsCorrectlyFiltered() throws Throwable {
        launchDefaultActivity(DUMMY_USERREF);

        DatabaseObject<Event> event1 = new DatabaseObject<>("event1", DUMMY_EVENT1);
        DatabaseObject<Event> event2 = new DatabaseObject<>("event2", DUMMY_EVENT2);
        DatabaseObject<Event> event3 = new DatabaseObject<>("event3", DUMMY_EVENT3);
        DatabaseObject<Event> event4 = new DatabaseObject<>("event4", DUMMY_EVENT4);

        mActivity.runOnUiThread(() -> {
            mAttendingEventsLiveData.setValue(Arrays.asList(event2));
            mEventsLiveData.setValue(Arrays.asList(event1, event2, event3, event4));
            mOwnedEventsLiveData.setValue(Arrays.asList(event3));
        });

        onView(withText(event4.getObject().getTitle())).check(matches(isDisplayed()));
    }
}
