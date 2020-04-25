package ch.epfl.sdp.ui.createevent;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class CreateEventViewModel extends ViewModel {

    static class CreateEventViewModelFactory extends DatabaseViewModelFactory { }

    interface OnEventCreatedCallback {
        void onSuccess(String eventRef);
        void onFailure(Exception exception);
    }

    private final CollectionQuery mEventCollection;
    private final Database mDatabase;

    public CreateEventViewModel(@NonNull Database database) {
        mDatabase = verifyNotNull(database);
        mEventCollection = mDatabase.query("events");
    }

    public void insertEvent(@NonNull Event event, @NonNull OnEventCreatedCallback callback) {
        mEventCollection.create(event)
                .then(callback::onSuccess)
                .except(callback::onFailure);
    }
}
