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
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private Database mDb;
    private MutableLiveData<String> mRef = new MutableLiveData<>();

    public LiveData<Event> getEvent(String ref) {
        if (mEvent == null) {
            mEvent = mDb.query("events").document(ref).livedata(Event.class);
        }
        return mEvent;
    }

    public String formatDate(Date date) {
        return mFormatter.format(date);
    }

    public LiveData<String> createEvent(@NonNull String title,
                                        @NonNull String description,
                                        @NonNull String date, @NonNull String address,
                                        @NonNull LatLng location) throws ParseException {
        Event newEvent = new Event(title, description, mFormatter.parse(date), address, location);
        mDb.query("events").create(newEvent, result -> {
            mRef.postValue(result.getData());
        });
        return mRef;
    }

    public void setDb(Database db) {
        this.mDb = db;
    }
}
