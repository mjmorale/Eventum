package ch.epfl.sdp.ui.event;

import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.firebase.db.FirestoreDatabase;

public class EventViewModel extends ViewModel {

    private final Database mDb;

    private LiveData<Event> mEvent = null;

    public EventViewModel() {
        mDb = new FirestoreDatabase(FirebaseFirestore.getInstance());
    }

    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mDb.query("events").document("SUmlKCzixFlmg8zB0Bpb").livedata(Event.class);
        }
        return mEvent;
    }
}
