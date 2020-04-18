package ch.epfl.sdp.ui.createevent;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.UiDevice;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.mocks.MockEvents;
import ch.epfl.sdp.mocks.MockFragmentFactory;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.clearText;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.closeSoftKeyboard;
import static androidx.test.espresso.action.ViewActions.pressBack;
import static androidx.test.espresso.action.ViewActions.scrollTo;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.toPackage;
import static androidx.test.espresso.matcher.RootMatchers.isPlatformPopup;
import static androidx.test.espresso.matcher.RootMatchers.withDecorView;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withHint;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.CoreMatchers.instanceOf;
import static org.hamcrest.CoreMatchers.not;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class CreateEventFragmentTest {
    private static final Event mMockEvent = MockEvents.getNextEvent();
    private static final String DATE = mMockEvent.getDateStr();
    private static final String TITLE = mMockEvent.getTitle();
    private static final String DESCRIPTION = mMockEvent.getDescription();
    private static final String ADDRESS = mMockEvent.getAddress();
    private static final String EMPTY = "";
    private static final int DAY = mMockEvent.getDate().getDay();
    private static final int MONTH = mMockEvent.getDate().getMonth();
    private static final int YEAR = mMockEvent.getDate().getYear();

    private Activity mActivity;
    private UiDevice mDevice = UiDevice.getInstance(InstrumentationRegistry.getInstrumentation());

    @Rule
    public GrantPermissionRule mPermissionFine =
            GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule
    public IntentsTestRule<CreateEventActivity> mIntentsTestRule =
            new IntentsTestRule<>(CreateEventActivity.class);

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            Event event = (Event) args[0];

            assertThat(event.getTitle(), is(TITLE));
            assertThat(event.getDescription(), is(DESCRIPTION));
            assertThat(event.getAddress(), containsString(ADDRESS));

            ((Query.OnQueryCompleteCallback) args[1]).onQueryComplete(QueryResult.success("fake"));
            return null;
        }).when(mCollectionQuery).create(any(), any());

        FragmentScenario<CreateEventFragment> scenario = FragmentScenario.launchInContainer(
                CreateEventFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(CreateEventFragment.class, mDatabase));

        scenario.onFragment(fragment -> {
            mActivity = fragment.getActivity();
        });
    }

    @Test
    public void CreateEventFragment_CorrectInput() {
        doCorrectInput();
    }

    @Test
    public void CreateEventFragment_IncorrectInput() {
        onView(withHint(is("title"))).perform(
                clearText(),
                typeText(EMPTY),
                closeSoftKeyboard());

        clickCreateButton();

        // Check Toast message is displayed
        onView(withText(R.string.toast_incorrect_input))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void CreateEventFragment_ImageNotSelected() {
        clickAddImageButton();

        mDevice.pressBack();

        // Check Toast message is displayed
        onView(withText(R.string.no_image_chosen))
                .inRoot(withDecorView(not(is(mActivity.getWindow().getDecorView()))))
                .check(matches(isDisplayed()));
    }

    @Test
    public void CreateEventFragment_CorrectIntentPermissions() {
        clickAddImageButton();

        intended(toPackage("com.google.android.permissioncontroller"));

        mDevice.pressBack();
    }

    @Test
    public void CreateEventFragment_CorrectIntentImageSelection() {
        clickAddImageButton();

        intended(toPackage("com.google.android.apps.photos"));

        mDevice.pressBack();
    }

    private void doCorrectInput() {
        onView(withId(R.id.title)).perform(
                typeText(TITLE),
                closeSoftKeyboard());

        onView(withId(R.id.description)).perform(
                typeText(DESCRIPTION),
                closeSoftKeyboard());

        onView(withId(R.id.date)).perform(
                PickerActions.setDate(YEAR, MONTH, DAY),
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
