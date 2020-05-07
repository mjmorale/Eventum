package ch.epfl.sdp.ui.main.attending;

import android.util.Log;

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
import ch.epfl.sdp.db.queries.FilterQuery;
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
        mAttendingLiveData = database.query("events").whereArrayContains("attendees", user.getUid()).liveData(Event.class);
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

    private void setupCachedEventLiveData() {
        mCachedEventsLiveData.addSource(mAttendingLiveData, databaseObjects -> {
            // If we are offline, do nothing
            if(databaseObjects != null) {
                // If online, update the cache
                for(DatabaseObject<Event> object: databaseObjects) {
                    try {
                        mCache.saveEvent(object.getObject(), object.getId(), object.getObject().getDate(), mCacheDir);
                    } catch (IOException | ClassNotFoundException e) {
                        Log.e(TAG, "Cannot save event to the cache", e);
                    }
                }
            }

            // Once the cache is up to date, return all events from the cache
            try {
                List<DatabaseObject<Event>> events = mCache.getAllEventsWithRefs(mCacheDir);
                mCachedEventsLiveData.postValue(events);
            } catch (IOException | ClassNotFoundException e) {
                Log.e(TAG, "Cannot read events from the cache", e);
            }
        });
    }
}