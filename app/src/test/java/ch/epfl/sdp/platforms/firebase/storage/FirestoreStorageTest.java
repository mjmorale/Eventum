package ch.epfl.sdp.platforms.firebase.storage;

import com.google.firebase.storage.FirebaseStorage;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreStorageTest {
    @Mock
    private FirebaseStorage mStorage;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirestoreStorage_Constructor_FailsWithNullArgument() {
        FirestoreStorage storage = new FirestoreStorage(null);
    }






}
