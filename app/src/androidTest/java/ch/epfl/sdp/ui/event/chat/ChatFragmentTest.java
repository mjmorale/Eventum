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

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.R;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.mocks.MockFragmentFactory;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.UIConstants;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.when;

// This is an example implementation of an instrumented fragment test.
@RunWith(MockitoJUnitRunner.class)
public class ChatFragmentTest {

    private  ChatFragment chatFragment = ChatFragment.getInstance("anyRef");
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

    private DatabaseObject<ChatMessage> mChatMessage = new DatabaseObject<>("fdgsetgserg", new ChatMessage("Hello", new Date(), "anyRef", " "));

    LiveData<List<DatabaseObject<ChatMessage>>> mLiveData = new LiveData<List<DatabaseObject<ChatMessage>>>() {
        @Override
        public void observe(@NonNull LifecycleOwner owner, @NonNull Observer<? super List<DatabaseObject<ChatMessage>>> observer) {
            observer.onChanged(Arrays.asList(mChatMessage));
        }
    };

    @Before
    public void setup() {
        // This function initializes the mocks before each test.
        MockitoAnnotations.initMocks(this);

        when(mDatabaseMock.query(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.document(anyString())).thenReturn(mDocumentQueryMock);
        when(mDocumentQueryMock.collection(anyString())).thenReturn(mCollectionQueryMock);
        when(mCollectionQueryMock.orderBy(anyString())).thenReturn(mFilterQueryMock);
        when(mFilterQueryMock.liveData(ChatMessage.class)).thenReturn(mLiveData);


        doAnswer(invocation -> {
            Object[] args = invocation.getArguments();
            ((Query.OnQueryCompleteCallback) args[1]).onQueryComplete(QueryResult.success("fake"));
            return null;
        }).when(mCollectionQueryMock).create(any(), any());

        when(mFirebaseAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo(mChatMessage.getObject().getUid(), " ", " "));

    }

    @SuppressWarnings("unchecked")
    @Test
    public void ChatFragment_Test_Sender() {
        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");
        FragmentScenario<ChatFragment> scenarioSender = FragmentScenario.launchInContainer(
                ChatFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(ChatFragment.class, mDatabaseMock, mChatMessage.getObject().getUid(), mFirebaseAuthenticatorMock)
        );

        onView(withId(R.id.layout_chatbox)).check(matches(isDisplayed()));
        onView(withId(R.id.edittext_chatbox)).perform(typeText(mChatMessage.getObject().getText()));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        FragmentScenario<ChatFragment> scenarioReceinver = FragmentScenario.launchInContainer(
                ChatFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(ChatFragment.class, mDatabaseMock, mChatMessage.getObject().getUid(), mFirebaseAuthenticatorMock)
        );
        when(mFirebaseAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo("newRef", " ", " "));

        onView(withId(R.id.layout_chatbox)).check(matches(isDisplayed()));
        onView(withId(R.id.edittext_chatbox)).perform(typeText(mChatMessage.getObject().getText()));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        onView(withText(mChatMessage.getObject().getText())).check(matches(isDisplayed()));
    }


    @SuppressWarnings("unchecked")
    @Test
    public void ChatFragment_Test_receiver() {
        when(mFirebaseAuthenticatorMock.getCurrentUser()).thenReturn(new UserInfo("newRef", " ", " "));

        Bundle bundle = new Bundle();
        bundle.putString(UIConstants.BUNDLE_EVENT_REF, "anyRef");

        FragmentScenario<ChatFragment> scenarioReceinver = FragmentScenario.launchInContainer(
                ChatFragment.class,
                bundle,
                R.style.Theme_AppCompat,
                new MockFragmentFactory(ChatFragment.class, mDatabaseMock, mChatMessage.getObject().getUid(), mFirebaseAuthenticatorMock)
        );

        onView(withId(R.id.layout_chatbox)).check(matches(isDisplayed()));
        onView(withId(R.id.edittext_chatbox)).perform(typeText(mChatMessage.getObject().getText()));
        onView(withId(R.id.button_chatbox_send)).perform(click());

        onView(withText(mChatMessage.getObject().getText())).check(matches(isDisplayed()));
    }



    //TODO: implement other tests.
}
