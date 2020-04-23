package ch.epfl.sdp.ui;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.storage.Storage;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public final class ServiceProvider {

    private static ServiceProvider mInstance;

    public static ServiceProvider getInstance() {
        if(mInstance == null) {
            mInstance = new ServiceProvider();
        }
        return mInstance;
    }

    private Database mDatabase;
    private Authenticator mAuthenticator;
    private Storage mStorage;

    private ServiceProvider() {
        mDatabase = new FirestoreDatabase(FirebaseFirestore.getInstance());
        mAuthenticator = new FirebaseAuthenticator(FirebaseAuth.getInstance());
        mStorage = new FirestoreStorage(FirebaseStorage.getInstance());
    }

    public void setDatabase(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }

    public void setStorage(@NonNull Storage storage) {
        mStorage = verifyNotNull(storage);
    }

    public Database getDatabase() {
        return mDatabase;
    }

    public Storage getStorage() {
        return mStorage;
    }

    public <T> void setAuthenticator(@NonNull Authenticator<T> authenticator) {
        mAuthenticator = verifyNotNull(authenticator);
    }

    public Authenticator getAuthenticator() {
        return mAuthenticator;
    }
}
