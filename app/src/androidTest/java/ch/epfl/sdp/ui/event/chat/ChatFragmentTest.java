package ch.epfl.sdp.ui.event.chat;

import android.os.Bundle;

import androidx.annotation.NonNull;
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

import java.util.List;
import java.util.logging.Filter;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.CoreMatchers.containsString;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

// This is an example implementation of an instrumented fragment test.
@RunWith(MockitoJUnitRunner.class)
public class ChatFragmentTest {

    // Annotate your mock with @Mock
    @Mock
    private Database mDatabaseMock;
    @Mock
    private CollectionQuery mCollectionQueryMock;
    @Mock
    private DocumentQuery mDocumentQueryMock;
    @Mock
    private FilterQuery mFilterQueryMock;
    @Mock
    private FirebaseAuthenticator mFirebaseAuthenticatorMock;

    @Before
    public void setup() {
        // This function initializes the mocks before each test.
        MockitoAnnotations.initMocks(this);

        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.collection(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.orderBy(anyString())).thenReturn(mFilterQueryMock);
        when(mFilterQueryMock.livedata(ChatMessage.class)).thenReturn(new LiveData<List<ChatMessage>>() {});

        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((Query.OnQueryCompleteCallback) args[1]).onQueryComplete(QueryResult.success("fake"));
            return null;
        }).when(mCollectionQueryMock).create(any(), any());

        when(mFirebaseAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo(" ", " ", " "));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void ChatFragment_Test() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        FragmentScenario<ChatFragment> scenario = FragmentScenario.launchInContainer(
                ChatFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(ChatFragment.class, mDatabaseMock, "anyRef", mFirebaseAuthenticatorMock)
        );

        onView(withId(R.id.layout_chatbox)).check(matches(isDisplayed()));
    }

    //TODO: implement other tests.
}
