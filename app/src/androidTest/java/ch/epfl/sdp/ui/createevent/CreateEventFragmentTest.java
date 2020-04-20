package ch.epfl.sdp.ui.createevent;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentValues;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;

import androidx.annotation.NonNull;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;
import androidx.test.rule.GrantPermissionRule;
import androidx.test.uiautomator.By;
import androidx.test.uiautomator.BySelector;
import androidx.test.uiautomator.UiDevice;
import androidx.test.uiautomator.UiObject;
import androidx.test.uiautomator.UiObject2;
import androidx.test.uiautomator.UiObjectNotFoundException;
import androidx.test.uiautomator.UiScrollable;
import androidx.test.uiautomator.UiSelector;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
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
import static androidx.test.espresso.intent.Intents.intending;
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

//    @Rule // delete ? in manifest too !!!
//    public GrantPermissionRule mPermissionWriteStorage =
//            GrantPermissionRule.grant(android.Manifest.permission.WRITE_EXTERNAL_STORAGE);

    @Rule
    public GrantPermissionRule mPermissionReadStorage =
            GrantPermissionRule.grant(android.Manifest.permission.READ_EXTERNAL_STORAGE);

    @Rule // delete if intents tests deleted
    public ActivityTestRule<CreateEventActivity> mIntentsTestRule =
            new ActivityTestRule<>(CreateEventActivity.class);

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Before
    public void setup() {

        Intents.init(); // delete if intents tests deleted
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

        // addImageInStorage(); // keep here ???????????????????????
    }

    @After
    public void after() { // delete if intents tests deleted
        Intents.release();
    }

    @Test
    public void CreateEventFragment_CorrectInput() {
        doCorrectInput();
    }

    @Test
    public void CreateEventFragment_IncorrectInput() throws UiObjectNotFoundException {
        UiScrollable appViews = new UiScrollable(new UiSelector().scrollable(true));
        appViews.scrollIntoView(new UiSelector().text("title"));

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

//    @Test
//    public void CreateEventFragment_CorrectIntentStoragePermissions() {
//        clickAddImageButton();
//
//        intended(toPackage("com.google.android.permissioncontroller"));
//
//        mDevice.pressBack();
//    }
//
//    @Test
//    public void CreateEventFragment_CorrectIntentImageSelection() {
//        clickAddImageButton();
//
//        intended(toPackage("com.google.android.apps.photos"));
//
//        mDevice.pressBack();
//    }

    @Test
    public void CreateEventFragment_CorrectImageSelection() throws IOException {
        // addImageInStorage();
        // clickAddImageButton();

        Uri uri = Uri.parse("android.resource://ch.epfl.sdp/drawable/add_image");
        //Bitmap bitmap = MediaStore.Images.Media.getBitmap(mActivity.getContentResolver() , Uri.parse("android.resource://ch.epfl.sdp/drawable/add_image"));

        Intent intent = new Intent();
        intent.setData(uri);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);
        intending(toPackage("com.google.android.apps.photos")).respondWith(result);
        clickAddImageButton();

        Intents.release();


//
//        UiSelector selector = new UiSelector().text("Download");
//        UiObject download = mDevice.findObject(selector);
//        download.click();


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

    private void addImageInStorage() {
        Bitmap bitmap = BitmapFactory.decodeResource(mActivity.getResources(), R.drawable.add_image);
//        File dir = mActivity.getExternalCacheDir();
//        File file = new File(dir, "imageTest.jpeg");
//        try {
//            FileOutputStream out = new FileOutputStream(file);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 90, out);
//            out.flush();
//            out.close();
//
//        } catch (Exception e) {
//            e.printStackTrace();
//        }


        MediaStore.Images.Media.insertImage(mActivity.getContentResolver(), bitmap, "testImage" , "for testing");  // Saves the image.

//        ContentValues values = new ContentValues();
//        values.put(MediaStore.Images.Media.DISPLAY_NAME, "testImage");
//        values.put(MediaStore.Images.Media.MIME_TYPE, "image/png");
//        values.put(MediaStore.Images.Media.IS_PENDING, 1);
//        values.put(MediaStore.MediaColumns.RELATIVE_PATH, Environment.DIRECTORY_PICTURES);


    }
}
