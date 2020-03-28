package ch.epfl.sdp.ui.createevent;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class CreateEventViewModel extends ViewModel {

    static class CreateEventViewModelFactory extends ParameterizedViewModelFactory {

        CreateEventViewModelFactory() {
            super(Database.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }
    }

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

    public void insertEvent(@NonNull String name, @NonNull String description, @NonNull String date, @NonNull OnEventCreatedCallback callback) throws ParseException {
        verifyNotNull(name, description, date, callback);
        Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        Event event = new Event(name, description, parsedDate);
        mEventCollection.create(event, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(res.getData());
            } else {
                callback.onFailure(res.getException());
            }
        });
    }
}
