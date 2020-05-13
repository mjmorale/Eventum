package ch.epfl.sdp.ui.event.createevent;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.mocks.MockEvents;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.storage.Storage;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.replaceText;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.core.AllOf.allOf;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateEventFragmentTest {

    private static final UserInfo DUMMY_USERINFO = new UserInfo("dummyuid", "dummyname", "dummyemail");
    private static final Event mMockEvent = MockEvents.getNextEvent();
    private static final String DATE = mMockEvent.getDateStr();
    private static final String TITLE = mMockEvent.getTitle();
    private static final String DESCRIPTION = mMockEvent.getDescription();
    private static final String ADDRESS = mMockEvent.getAddress();
    private static final String EMPTY = "";
    private static final int DAY = mMockEvent.getDate().getDay();
    private static final int MONTH = mMockEvent.getDate().getMonth();
    private static final int YEAR = mMockEvent.getDate().getYear();
    private static final int HOURS = mMockEvent.getDate().getHours();
    private static final int MINUTES = mMockEvent.getDate().getMinutes();


    private Activity mActivity;
    private UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    private FragmentScenario<CreateEventFragment> mScenario;

    @Rule
    public GrantPermissionRule mPermissionReadStorage =
            GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);

    @Mock
    private Database mDatabase;

    @Mock
    private Storage mStorage;

    @Mock
    private Authenticator mAuthenticator;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Before
    public void setup() {

        Intents.init();
        MockitoAnnotations.initMocks(this);

        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Event event = (Event) args[0];

            assertThat(event.getTitle(), is(TITLE));
            assertThat(event.getDescription(), is(DESCRIPTION));
            assertThat(event.getAddress(), containsString(ADDRESS));
            assertThat(event.getOrganizer(), is(DUMMY_USERINFO.getUid()));

            ((Query.OnQueryCompleteCallback) args[1]).onQueryComplete(QueryResult.success("fake"));
            return null;
        }).when(mCollectionQuery).create(any(), any());

        mScenario = FragmentScenario.launchInContainer(
                CreateEventFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(CreateEventFragment.class, mStorage, mDatabase, mAuthenticator));

        mScenario.onFragment(fragment -> {
            mActivity = fragment.getActivity();
        });
    }

    @After
    public void after() {
        Intents.release();
    }

    @Test
    public void CreateEventFragment_CorrectInput() {
        doCorrectInput();
    }

    @Test
    public void CreateEventFragment_IncorrectInput() throws UiObjectNotFoundException, InterruptedException {
        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.scrollIntoView(new UiSelector().text("title"));

        onView(withHint(is("title"))).perform(
                clearText(),
                replaceText(EMPTY),
                closeSoftKeyboard());

        clickCreateButton();

        // Check Toast message is displayed
        onView(withText(R.string.toast_incorrect_input))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));

        Thread.sleep(1000);
    }

    @Test
    public void CreateEventFragment_CorrectIntentImageSelection() throws InterruptedException {
        clickAddImageButton();

        intended(hasAction("android.intent.action.PICK"));

        mDevice.pressBack();

        // Check Toast message is displayed
        onView(withText(R.string.no_image_chosen))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
        Thread.sleep(1000);
    }

    @Test
    public void CreateEventFragment_CorrectImageSelection() {
        Uri uri = Uri.parse("android.resource://ch.epfl.sdp/drawable/add_image");
        Intent intent = new Intent();
        intent.setData(uri);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(hasAction("android.intent.action.PICK")).respondWith(result);
        clickAddImageButton();
       onView(withId(R.id.imageView)).check(matches(withTagValue(is((Object) "new_image"))));
    }

    private void doCorrectInput() {
        onView(withId(R.id.title)).perform(
                replaceText(TITLE),
                closeSoftKeyboard());

        onView(withId(R.id.description)).perform(
                replaceText(DESCRIPTION),
                closeSoftKeyboard());

        onView(withId(R.id.date)).perform(
                PickerActions.setDate(YEAR, MONTH, DAY),
                closeSoftKeyboard());

        onView(withId(R.id.time)).perform(
                PickerActions.setTime(HOURS, MINUTES),
                closeSoftKeyboard());

        onView(withId(R.id.geo_autocomplete)).perform(
                scrollTo(),
                typeText("Lausanne"));

        onData(instanceOf(GeoSearchResult.class))
                .inRoot(isPlatformPopup())
                .check(matches(isDisplayed()))
                .perform(click());

        clickCreateButton();
    }

    private void clickCreateButton() {
        onView(withId(R.id.createButton)).perform(
                scrollTo(),
                click());
    }

    private void clickAddImageButton() {
        onView(withId(R.id.addImageButton)).perform(
                scrollTo(),
                click());
    }
}
