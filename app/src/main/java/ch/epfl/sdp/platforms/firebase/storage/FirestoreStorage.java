package ch.epfl.sdp.platforms.firebase.storage;

import android.net.Uri;
import androidx.annotation.NonNull;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;
import java.util.UUID;
import ch.epfl.sdp.storage.Storage;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirestoreStorage implements Storage {

    private final FirebaseStorage mStorage;
    private String mUrl;

    public FirestoreStorage(@NonNull FirebaseStorage firebase) {
        mStorage = verifyNotNull(firebase);
    }

    public interface UrlReadyCallback {
        void onSuccess(String url);
        void onFailure();
    }

    @Override
    public void uploadImage(@NonNull Uri imageUri, @NonNull UrlReadyCallback callback) {
        String imageUUID = UUID.randomUUID().toString();
        mStorage.getReference(imageUUID).putFile(imageUri).addOnSuccessListener( (UploadTask.TaskSnapshot t) -> {
                Task<Uri> urlTask = t.getStorage().getDownloadUrl();
                while (!urlTask.isSuccessful());
                mUrl = urlTask.getResult().toString();
                callback.onSuccess(mUrl);
        }).addOnFailureListener( (Exception e) -> {
                callback.onFailure();
        });
    }
}
