package ch.epfl.sdp.platforms.firebase.storage;

import android.graphics.Bitmap;
import android.net.Uri;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.ByteArrayOutputStream;
import java.io.File;

import ch.epfl.sdp.offline.ImageCache;
import ch.epfl.sdp.storage.Storage;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.fail;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class FirestoreStorageTest {

    @Mock
    private FirebaseStorage mStorage;

    @Mock
    private UploadTask mUploadTask;

    @Mock
    private StorageReference mStorageReference;

    @Mock
    private ImageCache mImageCache;

    @Mock
    private Bitmap mBitmap;

    @Mock
    private Task<byte[]> mDownloadTask;

    @Mock
    private UploadTask.TaskSnapshot mTaskSnapshot;

    @Mock
    private File mCacheDir;

    @Captor
    private ArgumentCaptor<OnSuccessListener<UploadTask.TaskSnapshot>> mOnSuccessListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<OnSuccessListener<byte[]>> mOnByteSuccessListenerArgumentCaptor;

    @Captor
    private ArgumentCaptor<OnFailureListener> mOnFailureListenerArgumentCaptor;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_Constructor_FailsWithNullFirstArgument() {
        FirestoreStorage storage = new FirestoreStorage(null, mImageCache);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_Constructor_FailsWithNullSecondArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_UploadImage_FailsWithNullFirstArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage(null, mBitmap, 0, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_UploadImage_FailsWithNullSecondArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", null, 0, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_UploadImage_FailsWithNullThirdArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, 0, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_UploadImage_FailsWithCompressionLevelBelowZero() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, -1, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_UploadImage_FailsWithCompressionLevelOverAHundred() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, 101, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test
    public void FirestoreStorage_UploadImage_UsesCorrectCompressionParameters() {
        when(mBitmap.compress(any(), anyInt(), any())).thenReturn(false);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        int compression = 58;
        storage.uploadImage("folder", mBitmap, compression, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) { }
            @Override
            public void onFailure(Exception e) { }
        });
        verify(mBitmap).compress(any(), eq(compression), any());
    }

    @Test
    public void FirestoreStorage_UploadImage_CallsCallbackWithErrorIfCompressionFails() {
        when(mBitmap.compress(any(), anyInt(), any())).thenReturn(false);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, 100, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) {
                fail();
            }
            @Override
            public void onFailure(Exception e) {
                assertNotNull(e);
            }
        });
    }

    @Test
    public void FirestoreStorage_UploadImage_UsesCorrectFolderParameter() {
        when(mBitmap.compress(any(), anyInt(), any())).thenReturn(true);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.putBytes(any(), any())).thenReturn(mUploadTask);
        when(mUploadTask.addOnSuccessListener(any())).thenReturn(mUploadTask);
        when(mUploadTask.addOnFailureListener(any())).thenReturn(mUploadTask);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, 100, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) { }
            @Override
            public void onFailure(Exception e) { }
        });

        verify(mStorageReference).child(eq("folder"));
    }

    @Test
    public void FirestoreStorage_UploadImage_CallsSuccessCallbackOnSuccess() {
        when(mBitmap.compress(any(), anyInt(), any())).thenReturn(true);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.putBytes(any(), any())).thenReturn(mUploadTask);
        when(mUploadTask.addOnSuccessListener(mOnSuccessListenerArgumentCaptor.capture())).thenReturn(mUploadTask);
        when(mUploadTask.addOnFailureListener(any())).thenReturn(mUploadTask);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, 100, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) {
                assertNotNull(ref);
            }
            @Override
            public void onFailure(Exception e) {
                fail();
            }
        });

        mOnSuccessListenerArgumentCaptor.getValue().onSuccess(mTaskSnapshot);
    }

    @Test
    public void FirestoreStorage_UploadImage_CallsFailureCallbackOnFailure() {
        when(mBitmap.compress(any(), anyInt(), any())).thenReturn(true);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.putBytes(any(), any())).thenReturn(mUploadTask);
        when(mUploadTask.addOnSuccessListener(any())).thenReturn(mUploadTask);
        when(mUploadTask.addOnFailureListener(mOnFailureListenerArgumentCaptor.capture())).thenReturn(mUploadTask);

        Exception exception = new Exception();
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.uploadImage("folder", mBitmap, 100, new Storage.RefReadyCallback() {
            @Override
            public void onSuccess(String ref) {
                fail();
            }
            @Override
            public void onFailure(Exception e) {
                assertEquals(exception, e);
            }
        });

        mOnFailureListenerArgumentCaptor.getValue().onFailure(exception);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_DownloadImage_FailsWithNullFirstArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(null, "folder", "ref", 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_DownloadImage_FailsWithNullSecondArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, null, "ref", 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_DownloadImage_FailsWithNullThirdArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", null, 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_DownloadImage_FailsWithNullForthArgument() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 1, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void FirestoreStorage_DownloadImage_FailsWithInvalidFileSizeParameter() {
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 0, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });
    }

    @Test
    public void FirestoreStorage_DownloadImage_ReturnsFromCacheIfAvailable() {
        when(mImageCache.getImage(mCacheDir, "ref")).thenReturn(mBitmap);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                assertEquals(mBitmap, bitmap);
            }
            @Override
            public void onFailure(Exception e) {
                fail();
            }
        });
    }

    @Test
    public void FirestoreStorage_DownloadImage_UsesCorrectDownloadSize() {
        when(mImageCache.getImage(mCacheDir, "ref")).thenReturn(null);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.getBytes(anyLong())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnSuccessListener(any())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnFailureListener(any())).thenReturn(mDownloadTask);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 2, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });

        verify(mStorageReference).getBytes(eq((long)(2 * 1024 * 1024)));
    }

    @Test
    public void FirestoreStorage_DownloadImage_UsesCorrectFolderName() {
        when(mImageCache.getImage(mCacheDir, "ref")).thenReturn(null);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.getBytes(anyLong())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnSuccessListener(any())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnFailureListener(any())).thenReturn(mDownloadTask);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 2, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });

        verify(mStorageReference).child(eq("folder"));
    }

    @Test
    public void FirestoreStorage_DownloadImage_UsesCorrectImageRef() {
        when(mImageCache.getImage(mCacheDir, "ref")).thenReturn(null);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.getBytes(anyLong())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnSuccessListener(any())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnFailureListener(any())).thenReturn(mDownloadTask);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 2, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) { }
            @Override
            public void onFailure(Exception e) { }
        });

        verify(mStorageReference).child(eq("ref"));
    }

    @Test
    public void FirestoreStorage_DownloadImage_CallsFailureCallbackOnFailure() {
        when(mImageCache.getImage(mCacheDir, "ref")).thenReturn(null);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.getBytes(anyLong())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnSuccessListener(any())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnFailureListener(mOnFailureListenerArgumentCaptor.capture())).thenReturn(mDownloadTask);

        Exception exception = new Exception();
        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 2, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                fail();
            }
            @Override
            public void onFailure(Exception e) {
                assertEquals(exception, e);
            }
        });

        mOnFailureListenerArgumentCaptor.getValue().onFailure(exception);
    }

    @Test
    public void FirestoreStorage_DownloadImage_SavesImageInCacheOnSuccess() {
        when(mImageCache.getImage(mCacheDir, "ref")).thenReturn(null);
        when(mStorage.getReference()).thenReturn(mStorageReference);
        when(mStorageReference.child(anyString())).thenReturn(mStorageReference);
        when(mStorageReference.getBytes(anyLong())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnSuccessListener(mOnByteSuccessListenerArgumentCaptor.capture())).thenReturn(mDownloadTask);
        when(mDownloadTask.addOnFailureListener(any())).thenReturn(mDownloadTask);

        FirestoreStorage storage = new FirestoreStorage(mStorage, mImageCache);
        storage.downloadImage(mCacheDir, "folder", "ref", 2, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                verify(mImageCache).saveImage(any(), any(), any());
            }
            @Override
            public void onFailure(Exception e) {
                fail();
            }
        });

        mOnByteSuccessListenerArgumentCaptor.getValue().onSuccess(new byte[]{0});
    }
}
