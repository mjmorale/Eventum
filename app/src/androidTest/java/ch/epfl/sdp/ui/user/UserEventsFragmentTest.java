package ch.epfl.sdp.ui.user;

import android.os.Bundle;

import com.google.android.gms.maps.model.LatLng;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.lifecycle.MutableLiveData;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.ui.user.events.UserEventsFragment;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class UserEventsFragmentTest {

    private final static UserInfo DUMMY_USERINFO = new UserInfo("testuid", "testname", "testemail");
    private final static EventBuilder sEventBuilder = new EventBuilder();
    private final static Event DUMMY_EVENT = sEventBuilder
            .setTitle("testtitle")
            .setAddress("testaddress")
            .setDate(new Date())
            .setDescription("testdescription")
            .setImageId("testurl")
            .setOrganizerRef("testref")
            .setLocation(new LatLng(12.3, 45.6))
            .build();

    @Mock
    private Database mDatabase;

    @Mock
    private CollectionQuery mCollectionQuery;

    @Mock
    private FilterQuery mFilterQuery;

    @Mock
    private Authenticator mAuthenticator;

    private final MutableLiveData<List<DatabaseObject<Event>>> mEventList = new MutableLiveData<>();

    private FragmentScenario<UserEventsFragment> mScenario;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);

        when(mAuthenticator.getCurrentUser()).thenReturn(DUMMY_USERINFO);
        when(mDatabase.query(anyString())).thenReturn(mCollectionQuery);
        when(mCollectionQuery.whereFieldEqualTo(anyString(), any())).thenReturn(mFilterQuery);
        when(mFilterQuery.liveData(Event.class)).thenReturn(mEventList);

        mScenario = FragmentScenario.launchInContainer(
                UserEventsFragment.class,
                new Bundle(),
                R.style.Theme_AppCompat,
                new MockFragmentFactory<>(UserEventsFragment.class, mDatabase, mAuthenticator)
        );
    }

    @Test
    public void UserEventsFragment_EmptyMessageIsDisplayedIfNoEvents() {
        onView(withId(R.id.user_events_empty_msg)).check(matches(isDisplayed()));
    }

    @Test
    public void UserEventsFragment_EmptyMessageIsHiddenWhenListIsNotEmpty() {
        mScenario.onFragment(fragment -> {
            mEventList.setValue(Arrays.asList(new DatabaseObject<>("testuid", DUMMY_EVENT)));
        });

        onView(withId(R.id.user_events_empty_msg)).check(matches(not(isDisplayed())));
        onView(withText(DUMMY_EVENT.getTitle())).check(matches(isDisplayed()));
    }
}
