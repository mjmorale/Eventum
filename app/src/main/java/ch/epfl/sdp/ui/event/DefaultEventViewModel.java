package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventViewModel extends ViewModel {

    static class DefaultEventViewModelFactory extends DatabaseViewModelFactory {

        DefaultEventViewModelFactory() {
            super(String.class);
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }
    }

    private LiveData<Event> mEvent;

    private final DocumentQuery mEventDocumentQuery;
    private final Database mDatabase;

    public DefaultEventViewModel(@NonNull String eventRef, @NonNull Database database) {
        verifyNotNull(eventRef, database);
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
