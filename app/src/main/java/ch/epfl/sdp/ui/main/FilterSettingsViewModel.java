package ch.epfl.sdp.ui.main;

import android.location.Location;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.GeoPoint;

import java.util.Collection;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FilterSettingsViewModel extends ViewModel {
    static class FilterSettingsViewModelFactory extends ParameterizedViewModelFactory {
        FilterSettingsViewModelFactory() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

    private final CollectionQuery mEventQuery;

    private final MediatorLiveData<Collection<Event>> mResultsLiveData = new MediatorLiveData<>();

    public FilterSettingsViewModel(@NonNull Database database) {
        mEventQuery = database.query("events");
    }

    public LiveData<Collection<Event>> getFilteredEvents() {
        return mResultsLiveData;
    }

    public void setSettings(Location locationSetting, Double radiusSetting) {
        GeoPoint location = new GeoPoint(locationSetting.getLatitude(), locationSetting.getLongitude());
        LiveData<Collection<Event>> results =  mEventQuery.atLocation(location, radiusSetting).liveData(Event.class);
        mResultsLiveData.addSource(results, mResultsLiveData::postValue);
    }
}
