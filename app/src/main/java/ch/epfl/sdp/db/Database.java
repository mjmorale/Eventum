package ch.epfl.sdp.db;

import java.util.List;
import java.util.Map;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.auth.User;

public interface Database {

    interface OnQueryCompleteCallback<T> {
        void onQueryComplete(DatabaseQueryResult<T> result);
    }

    void loadEvents(OnQueryCompleteCallback<Event> callback);

    void loadUser(String uid, OnQueryCompleteCallback<User> callback);
}
