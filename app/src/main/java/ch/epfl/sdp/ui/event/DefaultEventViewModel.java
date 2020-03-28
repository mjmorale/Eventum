package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventViewModel extends ViewModel {

    private LiveData<Event> mEvent;

    private final DocumentQuery mEventDocumentQuery;
    private final Database mDatabase;

    public DefaultEventViewModel(@NonNull Database database, @NonNull String eventRef) {
        verifyNotNull(database, eventRef);
        mDatabase = database;
        mEventDocumentQuery = database.query("events").document(eventRef);
    }

    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mEventDocumentQuery.livedata(Event.class);
        }
        return mEvent;
    }

}
