package ch.epfl.sdp.ui.user.profile;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static junit.framework.TestCase.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class ProfileFragmentTest {

    final String USERNAME = "John Doe";

    final String DESCRIPTION = "Pretty common name";

    final String EMAIL = "john.doe@epfl.ch";

    final String IMAGEID = "1234";

    final String USERREF = "abcd";

    User user = new User(USERNAME, EMAIL, IMAGEID, DESCRIPTION);

    @Mock
    Database mDatabase;

    @Mock
    CollectionQuery mCollectionQuery;

    @Mock
    DocumentQuery mDocumentQuery;


    LiveData mLiveData = new LiveData() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer observer) {
            observer.onChanged(user);
        }
    };

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);

        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.document(anyString())).thenReturn(mDocumentQuery);
        when(mDocumentQuery.liveData(any())).thenReturn(mLiveData);

    }

    @Test
    public void ProfileFragment_DisplaysName(){
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_USER_REF, USERREF);

        FragmentScenario<ProfileFragment> scenario = FragmentScenario.launchInContainer(
                ProfileFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(ProfileFragment.class, mDatabase, USERREF)
        );
        onView(withText(USERNAME)).check(matches(isDisplayed()));
        onView(withText(DESCRIPTION)).check(matches(isDisplayed()));
        onView(withText(EMAIL)).check(matches(isDisplayed()));
        onView(withId(R.id.user_picture));

    }

    @Test
    public void ProfileFragment_GetInstanceReturnsCorrectFragmentAndArgument(){
        Fragment fragment = ProfileFragment.getInstance(USERREF);
        Bundle bundle = fragment.getArguments();
        assertEquals(bundle.getString(UIConstants.BUNDLE_USER_REF), USERREF);
    }

    @Test(expected = IllegalArgumentException.class)
    public void ProfileFragment_GetInstanceFailsOnNullString(){
        ProfileFragment.getInstance(null);
    }
}
