package ch.epfl.sdp.ui.main.attending;

import android.util.Log;

import com.google.firebase.firestore.FieldValue;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import java.io.File;
import java.io.IOException;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.offline.EventSaver;
import ch.epfl.sdp.offline.ObjectSaver;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model used for the list of events a user attends to
 */
public class AttendingListViewModel extends ViewModel {

    /**
     * Factory for the AttendingListViewModel
     */
    static class AttendingListViewModelFactory extends DatabaseViewModelFactory {

        public AttendingListViewModelFactory() {
            super(Authenticator.class, EventSaver.class, File.class);
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(0, verifyNotNull(authenticator));
        }

        public void setObjectSaver(@NonNull EventSaver eventSaver) {
            setValue(1, verifyNotNull(eventSaver));
        }

        public void setCacheDir(@NonNull File cacheDir) {
            setValue(2, verifyNotNull(cacheDir));
        }

        public File getCacheDir() {
            return (File)getValue(2);
        }
    }

    private final static String TAG = "AttendingListViewModel";

    private LiveData<List<DatabaseObject<Event>>> mAttendingLiveData;
    private MediatorLiveData<List<DatabaseObject<Event>>> mCachedEventsLiveData = new MediatorLiveData<>();
    private final CollectionQuery mEventCollection;
    private final String mUserRef;

    private final File mCacheDir;
    private final EventSaver mCache;

    /**
     * Constructor of the AttendingListViewModel, the factory should be used instead of this
     *
     * @param authenticator The authentication service to use
     * @param database The database service to use
     */
    public AttendingListViewModel(@NonNull Authenticator authenticator, @NonNull EventSaver eventSaver, @NonNull File cacheDir, @NonNull Database database) {
        mCache = eventSaver;
        mCacheDir = cacheDir;
        UserInfo user = authenticator.getCurrentUser();
        mUserRef = user.getUid();
        mEventCollection = database.query("events");
        mAttendingLiveData = mEventCollection.whereArrayContains("attendees", mUserRef).liveData(Event.class);
        setupCachedEventLiveData();
    }

    /**
     * Method to get the list of events a user attends to
     *
     * @return a live list of events
     */
    public LiveData<List<DatabaseObject<Event>>> getAttendingEvents() {
        return mCachedEventsLiveData;
    }

    /**
     * Unsubscribe the current user to a given event reference.
     *
     * @param eventRef The reference of the event to leave.
     */
    public void leaveEvent(String eventRef) {
        Event removed = mCache.getSingleFile(eventRef, mCacheDir);
        mCache.removeSingleEvent(eventRef, mCacheDir);
        mEventCollection.document(eventRef).update("attendees", FieldValue.arrayRemove(mUserRef), result -> {
            if(!result.isSuccessful()) {
                // If the user could not be removed from the event, restore the cache
                mCache.saveEvent(removed, eventRef, removed.getDate(), mCacheDir);
                postCurrentCacheContent();
            }
        });
    }

    private void setupCachedEventLiveData() {
        mCachedEventsLiveData.addSource(mAttendingLiveData, databaseObjects -> {
            // If we are offline, do nothing
            if(databaseObjects != null) {
                // If online, update the cache
                for(DatabaseObject<Event> object: databaseObjects) {
                    mCache.saveEvent(object.getObject(), object.getId(), object.getObject().getDate(), mCacheDir);
                }
            }
            // Once the cache is up to date, return all events from the cache
            postCurrentCacheContent();
        });
    }

    private void postCurrentCacheContent() {
        List<DatabaseObject<Event>> events = mCache.getAllEventsWithRefs(mCacheDir);
        mCachedEventsLiveData.postValue(events);
    }
}