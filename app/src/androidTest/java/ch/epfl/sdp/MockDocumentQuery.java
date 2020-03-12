package ch.epfl.sdp;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;

public class MockDocumentQuery implements DocumentQuery {
    private MockEvents mockEvents = new MockEvents();

    @Override
    public CollectionQuery collection(String collection) {
        return null;
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback) {

    }

    @Override
    public <T> LiveData<T> livedata(@NonNull Class<T> type) {
        if (type == Event.class) {
            return new LiveData() {
                @Override
                protected void onActive() {
                    super.onActive();
                    setValue(mockEvents.getNextEvent());
                }
            };
        }
        return null;
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {

    }
}
