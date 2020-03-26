package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;

public class EventViewModel extends ViewModel {
    private LiveData<Event> mEvent;
    private Database mDb;
    private MutableLiveData<String> mRef = new MutableLiveData<>();

    public LiveData<Event> getEvent(String ref) {
        if (mEvent == null) {
            mEvent = mDb.query("events").document(ref).livedata(Event.class);
        }
        return mEvent;
    }

    public LiveData<String> createEvent(@NonNull Event newEvent) {
        mDb.query("events").create(newEvent, result -> {
            mRef.postValue(result.getData());
        });
        return mRef;
    }

    public void setDb(Database db) {
        this.mDb = db;
    }
}
