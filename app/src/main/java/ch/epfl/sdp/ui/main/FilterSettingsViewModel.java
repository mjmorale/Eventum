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
import java.util.Collections;
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

public class FilterSettingsViewModel extends ViewModel {

    public static class FilterSettingsViewModelFactory extends DatabaseViewModelFactory {
        public FilterSettingsViewModelFactory() {
            super(LocationService.class, Authenticator.class);
        }

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
    private LiveData<List<DatabaseObject<Event>>> mAttendedEvents;

    private Collection<DatabaseObject<Event>> mToAdd = new ArrayList<>();
    private Collection<DatabaseObject<Event>> mToRemove = new ArrayList<>();

    private Map<String, DatabaseObject<Event>> mEvents = new ConcurrentHashMap<>();

    public FilterSettingsViewModel(@NonNull LocationService locationService, @NonNull Authenticator authenticator, @NonNull Database database) {
        mUserInfo = authenticator.getCurrentUser();
        mEventQuery = database.query("events");
        mRootLiveDataSource = mEventQuery.liveData(Event.class);
        mAttendedEvents = mEventQuery.whereArrayContains("attendees", mUserInfo.getUid()).liveData(Event.class);
        mResultsLiveData.addSource(mAttendedEvents, databaseObjects -> {
            mToRemove = databaseObjects;
            combineEvents();
            postCurrentEvents();
        });
        mResultsLiveData.addSource(mRootLiveDataSource, databaseObjects -> {
            mToAdd = databaseObjects;
            combineEvents();
            postCurrentEvents();
        });
        mLocationService = locationService;
    }

    public LiveData<Collection<DatabaseObject<Event>>> getFilteredEvents() {
        return mResultsLiveData;
    }

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
            mToAdd = databaseObjects;
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
        for(DatabaseObject<Event> event: mToAdd) {
            mEvents.put(event.getId(), event);
        }
        for(DatabaseObject<Event> event: mToRemove) {
            mEvents.remove(event.getId());
        }
    }

    private void postCurrentEvents() {
        mResultsLiveData.postValue(mEvents.values());
    }
}
