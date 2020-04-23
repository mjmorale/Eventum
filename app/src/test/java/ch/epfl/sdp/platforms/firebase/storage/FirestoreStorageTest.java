package ch.epfl.sdp.platforms.firebase.storage;

import android.net.Uri;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;


@RunWith(MockitoJUnitRunner.class)
public class FirestoreStorageTest {
    @Mock
    private FirebaseStorage mStorage;

    @Mock
    private Uri mUri;

    @Mock
    private FirestoreStorage.UrlReadyCallback mCallback;

    @Mock
    private UploadTask mTask;

    @Mock
    private StorageReference mRef;

    @Mock
    private StorageTask mStTask;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void FirestoreStorage_Constructor_FailsWithNullArgument() {
        FirestoreStorage storage = new FirestoreStorage(null);
    }

    @Test
    public void FirestoreStorage_ListenersAddedOnUploadImage() {
        FirestoreStorage storage = new FirestoreStorage(mStorage);

        FirestoreStorage.UrlReadyCallback callback = new FirestoreStorage.UrlReadyCallback() {
            @Override
            public void onSuccess(String url) { }

            @Override
            public void onFailure() { }
        };

        when(mStorage.getReference(anyString())).thenReturn(mRef);
        when(mRef.putFile(mUri)).thenReturn(mTask);
        when(mTask.addOnSuccessListener(any())).thenReturn(mStTask);

        storage.uploadImage(mUri, callback);

        verify(mTask).addOnSuccessListener(any());
        verify(mStTask).addOnFailureListener(any());
    }
}
