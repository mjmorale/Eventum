package ch.epfl.sdp.ui;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreSettings;
import com.google.firebase.storage.FirebaseStorage;

import androidx.annotation.NonNull;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.offline.EventSaver;
import ch.epfl.sdp.offline.ImageCache;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.platforms.firebase.db.FirestoreDatabase;
import ch.epfl.sdp.platforms.firebase.storage.FirestoreStorage;
import ch.epfl.sdp.storage.Storage;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Service to provide a database, an authenticator and a storage
 */
public final class ServiceProvider {

    private static ServiceProvider mInstance;

    /**
     * Method to get the instance of the provider
     *
     * @return the instance of the provider
     */
    public static ServiceProvider getInstance() {
        if(mInstance == null) {
            mInstance = new ServiceProvider();
        }
        return mInstance;
    }

    private Database mDatabase;
    private Authenticator mAuthenticator;
    private Storage mStorage;

    /**
     * Constructor of the ServiceProvider
     */
    private ServiceProvider() {
        mDatabase = new FirestoreDatabase(FirebaseFirestore.getInstance());
        mAuthenticator = new FirebaseAuthenticator(FirebaseAuth.getInstance());
        mStorage = new FirestoreStorage(FirebaseStorage.getInstance(), ImageCache.getInstance());
    }

    /**
     * Method to set the database to the provider
     *
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    public void setDatabase(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
    }

    /**
     * Method to set the storage to the provider
     *
     * @param storage {@link ch.epfl.sdp.storage.Storage}
     */
    public void setStorage(@NonNull Storage storage) {
        mStorage = verifyNotNull(storage);
    }

    /**
     * Method to get the database from the provider
     *
     * @return the database of the provider
     */
    public Database getDatabase() {
        return mDatabase;
    }

    /**
     * Method to get the storage from the provider
     *
     * @return the storage of the provider
     */
    public Storage getStorage() {
        return mStorage;
    }

    /**
     * Method to set the authenticator of the provider
     *
     * @param authenticator {@link ch.epfl.sdp.auth.Authenticator}
     */
    public <T> void setAuthenticator(@NonNull Authenticator<T> authenticator) {
        mAuthenticator = verifyNotNull(authenticator);
    }

    /**
     * Method to get the authenticator from the provider
     *
     * @return the authenticator of the provider
     */
    public Authenticator getAuthenticator() {
        return mAuthenticator;
    }
}
