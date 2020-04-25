package ch.epfl.sdp.ui.main;

import android.content.Context;
import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FilterSettingsViewModel extends ViewModel {
    public static class FilterSettingsViewModelFactory extends DatabaseViewModelFactory {
        public FilterSettingsViewModelFactory() {
            super(LocationService.class);
        }

        public void setLocationService(@NonNull LocationService locationService) {
            setValue(0, verifyNotNull(locationService));
        }
    }

    private final CollectionQuery mEventQuery;
    private final MediatorLiveData<List<Event>> mResultsLiveData = new MediatorLiveData<>();
    private final LocationService mLocationService;

    private LiveData<List<Event>> mCurrentLivedataSource;
    private LiveData<List<Event>> mRootLiveDataSource;

    public FilterSettingsViewModel(@NonNull LocationService locationService, @NonNull Database database) {
        mEventQuery = database.query("events");
        mRootLiveDataSource = mEventQuery.liveData(Event.class);
        mResultsLiveData.addSource(mRootLiveDataSource, mResultsLiveData::postValue);
        mLocationService = locationService;
    }

    public LiveData<List<Event>> getFilteredEvents() {
        return mResultsLiveData;
    }

    public void setSettings(Context context, Double radiusSetting) {
        Location location = mLocationService.getLastKnownLocation(context);
        if (mRootLiveDataSource != null) {
            mResultsLiveData.removeSource(mRootLiveDataSource);
            mRootLiveDataSource = null;
        } else {
            mResultsLiveData.removeSource(mCurrentLivedataSource);
        }
        mCurrentLivedataSource =
                mEventQuery.atLocation(location.getLatitude(), location.getLongitude(), radiusSetting).liveData(Event.class);
        mResultsLiveData.postValue(new ArrayList<>());
        mResultsLiveData.addSource(mCurrentLivedataSource, mResultsLiveData::postValue);
    }
}
