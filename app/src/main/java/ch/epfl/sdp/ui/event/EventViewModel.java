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
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

public class EventViewModel extends ViewModel {
    public LiveData<Event> getEvent(String ref) {
        if (event == null) {
            event = db.query("events").document(ref).livedata(Event.class);
        }
        return event;
    }

    public String formatDate(Date date) {
        return formatter.format(date);
    }

    public void createEvent(@NonNull String title,
                            @NonNull String description,
                            @NonNull String date) throws ParseException {
        Date formatDate = formatter.parse(date);
        Event newEvent = new Event(title, description, formatDate);
        db.query("events").create(newEvent, result -> {
            ref = result.getData();
        });
    }

    public void setDb(Database db) {
        this.db = db;
    }

    private LiveData<Event> event;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
    private Database db = new FirestoreDatabase(FirebaseFirestore.getInstance());
    private String ref;
}
