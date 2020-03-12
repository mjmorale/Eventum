package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.FirebaseFirestore;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventDatabaseBuilder;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

public class EventViewModel extends ViewModel {
    private LiveData<Event> mEvent;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");
    private Database mDb = new FirestoreDatabase(FirebaseFirestore.getInstance());
    private MutableLiveData<String> mRef = new MutableLiveData<>();

    public EventViewModel() {
        if (DatabaseObjectBuilderFactory.getBuilder(Event.class) == null) {
            try {
                DatabaseObjectBuilderFactory.registerBuilder(Event.class, EventDatabaseBuilder.class);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

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
                            @NonNull String date) throws ParseException {
        Date formatDate = mFormatter.parse(date);
        Event newEvent = new Event(title, description, formatDate);
        mDb.query("events").create(newEvent, result -> {
            mRef.postValue(result.getData());
        });
        return mRef;
    }

    public void setDb(Database db) {
        this.mDb = db;
    }
}
