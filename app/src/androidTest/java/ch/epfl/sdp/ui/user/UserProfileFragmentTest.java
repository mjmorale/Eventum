package ch.epfl.sdp.ui.user;

import android.os.Bundle;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

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
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

public class UserProfileFragmentTest {

    @Mock
    private DocumentQuery mDocumentQueryMock;

    @Mock
    private MutableLiveData<User> mUserLiveMock;

    @Mock
    private CollectionQuery mCollectionQueryMock;

    @Mock
    private Storage mStorageMock;

    private User mUser = new User("name", "email");

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @SuppressWarnings("unchecked")
    @Test
    public void UserProfileFragment_test1() {

        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.liveData(User.class)).thenReturn(mUserLiveMock);


        FragmentScenario<UserProfileFragment> scenario = FragmentScenario.launchInContainer(
                UserProfileFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory(UserProfileFragment.class, "AnyUserRef", mCollectionQueryMock, mStorageMock)
        );

        scenario.onFragment(fragment -> {mUserLiveMock.setValue(mUser);});

        onView(withId(R.id.user_profile_name)).check(matches(isDisplayed()));

    }

}
