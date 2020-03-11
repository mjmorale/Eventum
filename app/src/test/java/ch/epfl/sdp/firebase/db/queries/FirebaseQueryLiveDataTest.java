package ch.epfl.sdp.firebase.db.queries;

import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.firebase.db.MockStringBuilder;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirebaseQueryLiveDataTest {

    @Mock
    private Query mQuery;

    @Mock
    private ListenerRegistration mListenerRegistration;

    @Captor
    private ArgumentCaptor<EventListener<QuerySnapshot>> mEventListenerArgumentCaptor;

    @Before
    public void setup() throws IllegalAccessException, InstantiationException {
        MockitoAnnotations.initMocks(this);
        DatabaseObjectBuilderFactory.clear();
        DatabaseObjectBuilderFactory.registerBuilder(String.class, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseQueryLiveData_Constructor_FailsWithNullFirstArgument() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(null, String.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirebaseQueryLiveData_Constructor_FailsWithNullSecondArgument() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, null);
    }

    @Test
    public void FirebaseQueryLiveData_Constructor_CreationDoesNotFail() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
    }

    @Test
    public void FirebaseQueryLiveData_OnActive_DoesNotFailWithNullParameters() {
        when(mQuery.addSnapshotListener(mEventListenerArgumentCaptor.capture())).thenReturn(mListenerRegistration);

        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onActive();

        mEventListenerArgumentCaptor.getValue().onEvent(null, null);
    }

    @Test
    public void FirebaseQueryLiveData_OnInactive_RemovesListenerRegistrationIfSet() {
        when(mQuery.addSnapshotListener(any())).thenReturn(mListenerRegistration);

        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onActive();

        firebaseQueryLiveData.onInactive();
        verify(mListenerRegistration).remove();
    }

    @Test
    public void FirebaseQueryLiveData_OnInactive_DoesNotFailWithNullListener() {
        FirebaseQueryLiveData<String> firebaseQueryLiveData = new FirebaseQueryLiveData<>(mQuery, String.class);
        firebaseQueryLiveData.onInactive();
    }
}
