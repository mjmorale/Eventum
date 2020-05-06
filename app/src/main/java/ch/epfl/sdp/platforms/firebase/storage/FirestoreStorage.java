package ch.epfl.sdp.platforms.firebase.storage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.SimpleTarget;
import com.bumptech.glide.request.transition.Transition;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.UploadTask;

import java.io.File;
import java.io.FileOutputStream;
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
                urlTask.addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        mUrl = task.getResult().toString();
                        callback.onSuccess(mUrl);
                    } else callback.onFailure();
                }
            );
        }).addOnFailureListener( (Exception e) -> {
                callback.onFailure();
        });
    }


}
