package ch.epfl.sdp.ui.event;

import android.util.Log;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Date;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.DatabaseEventBuilder;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

public class EventViewModel extends ViewModel {

    private final Database mDb;

    private LiveData<Event> mEvent = null;

    public EventViewModel() {
        mDb = new FirestoreDatabase(FirebaseFirestore.getInstance());
        mDb.query("events").create(new Event("Title", "Description", new Date(2020, 12, 12)), result -> {
            Log.d("DEBUG", result.getData());
        });
    }

    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mDb.query("events").document("SUmlKCzixFlmg8zB0Bpb").livedata(Event.class);
        }
        return mEvent;
    }
}
