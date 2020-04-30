package ch.epfl.sdp.ui.main;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the filtering of events
 */
public class FilterSettingsViewModel extends ViewModel {

    /**
     * Factory for the FilterSettingsViewModel
     */
    public static class FilterSettingsViewModelFactory extends DatabaseViewModelFactory {
        /**
         * Constructor of the FilterSettingsViewModel factory
         */
        public FilterSettingsViewModelFactory() {
            super(LocationService.class, Authenticator.class);
        }

        /**
         * Method to set the locationService to the FilterSettingsViewModel factory
         *
         * @param locationService {@link ch.epfl.sdp.map.LocationService}
         */
        public void setLocationService(@NonNull LocationService locationService) {
            setValue(0, verifyNotNull(locationService));
        }

        public void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }
    }

    private final CollectionQuery mEventQuery;
    private final LocationService mLocationService;
    private final UserInfo mUserInfo;

    private final MediatorLiveData<Collection<DatabaseObject<Event>>> mResultsLiveData = new MediatorLiveData<>();
    private LiveData<Collection<DatabaseObject<Event>>> mCurrentLivedataSource;
    private LiveData<List<DatabaseObject<Event>>> mRootLiveDataSource;
    private LiveData<List<DatabaseObject<Event>>> mAttendedEventsLiveData;
    private LiveData<List<DatabaseObject<Event>>> mOwnedEventsLiveData;

    private Collection<DatabaseObject<Event>> mFilteredEvents = new ArrayList<>();
    private Collection<DatabaseObject<Event>> mAttendedEvents = new ArrayList<>();
    private Collection<DatabaseObject<Event>> mOwnedEvents = new ArrayList<>();

    private Map<String, DatabaseObject<Event>> mEvents = new ConcurrentHashMap<>();

    /**
     * Constructor of the FilterSettingsViewModel, the factory should be used instead of this
     *
     * @param locationService The location service to use
     * @param authenticator The authentication service to use
     * @param database The database service to use
     */
    public FilterSettingsViewModel(@NonNull LocationService locationService, @NonNull Authenticator authenticator, @NonNull Database database) {
        mUserInfo = authenticator.getCurrentUser();
        mEventQuery = database.query("events");
        mRootLiveDataSource = mEventQuery.liveData(Event.class);
        mAttendedEventsLiveData = mEventQuery.whereArrayContains("attendees", mUserInfo.getUid()).liveData(Event.class);
        mOwnedEventsLiveData = mEventQuery.whereFieldEqualTo("organizer", mUserInfo.getUid()).liveData(Event.class);
        mResultsLiveData.addSource(mAttendedEventsLiveData, databaseObjects -> {
            mAttendedEvents = databaseObjects;
            combineEvents();
            postCurrentEvents();
        });
        mResultsLiveData.addSource(mOwnedEventsLiveData, databaseObjects -> {
            mOwnedEvents = databaseObjects;
            combineEvents();
            postCurrentEvents();
        });
        mResultsLiveData.addSource(mRootLiveDataSource, databaseObjects -> {
            mFilteredEvents = databaseObjects;
            combineEvents();
            postCurrentEvents();
        });
        mLocationService = locationService;
    }

    /**
     * Method to get the events after the filtering
     *
     * @return the filtered events
     */
    public LiveData<Collection<DatabaseObject<Event>>> getFilteredEvents() {
        return mResultsLiveData;
    }

    /**
     * Method to set the filtering settings for events
     *
     * @param context the environment the application is currently running in
     * @param radiusSetting the radius (km) used for filtering the events
     */
    public void setSettings(Context context, Double radiusSetting) {
        Location location = mLocationService.getLastKnownLocation(context);
        GeoPoint locationGeoPoint = new GeoPoint(location.getLatitude(), location.getLongitude());
        if (mRootLiveDataSource != null) {
            mResultsLiveData.removeSource(mRootLiveDataSource);
            mRootLiveDataSource = null;
        } else {
            mResultsLiveData.removeSource(mCurrentLivedataSource);
        }
        mCurrentLivedataSource = mEventQuery.atLocation(locationGeoPoint, radiusSetting).liveData(Event.class);
        mResultsLiveData.postValue(new ArrayList<>());
        mResultsLiveData.addSource(mCurrentLivedataSource, databaseObjects -> {
            mFilteredEvents = databaseObjects;
            combineEvents();
            postCurrentEvents();
        });
    }

    public void joinEvent(@NonNull String eventRef, @NonNull Query.OnQueryCompleteCallback<Void> callback) {
        verifyNotNull(eventRef, callback);
        mEventQuery.document(eventRef).update("attendees", FieldValue.arrayUnion(mUserInfo.getUid()), callback);
    }

    private void combineEvents() {
        mEvents.clear();
        for(DatabaseObject<Event> event: mFilteredEvents) {
            mEvents.put(event.getId(), event);
        }
        for(DatabaseObject<Event> event: mAttendedEvents) {
            mEvents.remove(event.getId());
        }
        for(DatabaseObject<Event> event: mOwnedEvents) {
            mEvents.remove(event.getId());
        }
    }

    private void postCurrentEvents() {
        mResultsLiveData.postValue(mEvents.values());
    }
}
