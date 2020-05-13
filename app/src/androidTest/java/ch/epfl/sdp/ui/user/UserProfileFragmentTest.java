package ch.epfl.sdp.ui.user;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

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

import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.storage.Storage;
import ch.epfl.sdp.ui.user.profile.UserProfileFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withTagValue;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserProfileFragmentTest {

    @Mock
    private DocumentQuery mDocumentQueryMock;



    @Mock
    private CollectionQuery mCollectionQueryMock;

    @Mock
    private Storage mStorageMock;

    private User mUser = new User("name", "email");

    LiveData<User> mUserLive = new LiveData<User>() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super User> observer) {
            observer.onChanged(mUser);
        }
    };
    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.liveData(User.class)).thenReturn(mUserLive);

    }

    @SuppressWarnings("unchecked")
    @Test
    public void UserProfileFragment_test1() {


        FragmentScenario<UserProfileFragment> scenario = FragmentScenario.launchInContainer(
                UserProfileFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(UserProfileFragment.class, "AnyUserRef", mCollectionQueryMock, mStorageMock)
        );

//        scenario.onFragment(fragment -> {mUserLiveMock.setValue(mUser);});

        onView(withId(R.id.user_profile_photo)).check(matches(isDisplayed()));

        Uri uri = Uri.parse("android.resource://ch.epfl.sdp/drawable/add_image");
        Intent intent = new Intent();
        intent.setData(uri);
        Instrumentation.ActivityResult result =
                new Instrumentation.ActivityResult(Activity.RESULT_OK, intent);

        Intents.init();
        intending(hasAction("android.intent.action.PICK")).respondWith(result);
        onView(withId(R.id.user_profile_photo)).perform(click());
        Intents.release();

        onView(withId(R.id.user_profile_photo)).check(matches(withTagValue(is((Object) "new_image"))));
    }

}
