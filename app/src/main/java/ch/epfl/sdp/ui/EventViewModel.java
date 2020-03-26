package ch.epfl.sdp.ui;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;

public class EventViewModel extends DatabaseViewModel {

    protected final String mEventRef;

    public EventViewModel(@NonNull Database database, @NonNull String eventRef) {
        super(database);

        if(eventRef == null) {
            throw new IllegalArgumentException();
        }
        mEventRef = eventRef;
    }
}
