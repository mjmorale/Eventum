package ch.epfl.sdp;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;

public class CreateEventViewModel extends DatabaseViewModel {

    interface OnEventCreatedCallback {
        void onSuccess(Event event);
        void onFailure(Exception exception);
    }

    private final CollectionQuery mEventCollection;

    public CreateEventViewModel(@NonNull Database database) {
        super(database);
        mEventCollection = mDatabase.query("events");
    }

    public void insertEvent(@NonNull String name, @NonNull String description, @NonNull String date, @NonNull OnEventCreatedCallback callback) throws ParseException {
        if(callback == null) {
            throw new IllegalArgumentException();
        }
        Date parsedDate = new SimpleDateFormat("dd/MM/yyyy").parse(date);
        Event event = new Event(name, description, parsedDate);
        mEventCollection.create(event, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(event);
            } else {
                callback.onFailure(res.getException());
            }
        });
    }
}
