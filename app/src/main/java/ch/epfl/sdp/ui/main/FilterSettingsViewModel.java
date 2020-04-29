package ch.epfl.sdp.ui.main;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.GeoPoint;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
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
            super(LocationService.class);
        }

        /**
         * Method to set the locationService to the FilterSettingsViewModel factory
         *
         * @param locationService {@link ch.epfl.sdp.map.LocationService}
         */
        public void setLocationService(@NonNull LocationService locationService) {
            setValue(0, verifyNotNull(locationService));
        }
    }

    private final CollectionQuery mEventQuery;
    private final MediatorLiveData<Collection<Event>> mResultsLiveData = new MediatorLiveData<>();
    private final LocationService mLocationService;

    private LiveData<Collection<Event>> mCurrentLivedataSource;
    private LiveData<List<Event>> mRootLiveDataSource;

    /**
     * Constructor of the FilterSettingsViewModel, the factory should be used instead of this
     *
     * @param locationService {@link ch.epfl.sdp.map.LocationService}
     * @param database {@link ch.epfl.sdp.db.Database}
     */
    public FilterSettingsViewModel(@NonNull LocationService locationService, @NonNull Database database) {
        mEventQuery = database.query("events");
        mRootLiveDataSource = mEventQuery.liveData(Event.class);
        mResultsLiveData.addSource(mRootLiveDataSource, mResultsLiveData::postValue);
        mLocationService = locationService;
    }

    /**
     * Method to get the events after the filtering
     *
     * @return the filtered events
     */
    public LiveData<Collection<Event>> getFilteredEvents() {
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
        mCurrentLivedataSource =
                mEventQuery.atLocation(locationGeoPoint, radiusSetting).liveData(Event.class);
        mResultsLiveData.postValue(new ArrayList<>());
        mResultsLiveData.addSource(mCurrentLivedataSource, mResultsLiveData::postValue);
    }
}
