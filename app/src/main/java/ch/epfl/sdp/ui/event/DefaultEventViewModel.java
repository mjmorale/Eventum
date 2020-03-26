package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.EventViewModel;

public class DefaultEventViewModel extends EventViewModel {

    private LiveData<Event> mEvent;

    private DocumentQuery mEventDocumentQuery;

    public DefaultEventViewModel(@NonNull Database database, @NonNull String eventRef) {
        super(database, eventRef);

        mEventDocumentQuery = mDatabase.query("events").document(mEventRef);
    }

    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mEventDocumentQuery.livedata(Event.class);
        }
        return mEvent;
    }

}
