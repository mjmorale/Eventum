package ch.epfl.sdp.ui.event;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class DefaultEventViewModel extends ViewModel {

    static class DefaultEventViewModelFactory extends ParameterizedViewModelFactory {

        DefaultEventViewModelFactory() {
            super(Database.class, String.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(1, verifyNotNull(eventRef));
        }
    }

    private LiveData<Event> mEvent;

    private final DocumentQuery mEventDocumentQuery;
    private final Database mDatabase;
    private final String mEventRef;

    public DefaultEventViewModel(@NonNull Database database, @NonNull String eventRef) {
        verifyNotNull(database, eventRef);
        mDatabase = database;
        mEventRef = eventRef;
        mEventDocumentQuery = database.query("events").document(eventRef);
    }

    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mEventDocumentQuery.livedata(Event.class);
        }
        return mEvent;
    }

    public String getEventRef(){
        return mEventRef;
    }
}
