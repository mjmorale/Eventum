package ch.epfl.sdp.platforms.firebase.db.queries;


import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.offline.EventSaver;
import ch.epfl.sdp.offline.ObjectSaver;

public class OfflineDocumentQuery<T> extends FirebaseQuery implements DocumentQuery {
    private final DocumentReference mDocReference;
    private final FirebaseDocumentQuery mFirebaseDocumentQuery;


    public OfflineDocumentQuery(@NonNull FirebaseFirestore database, DocumentReference reference) {
        super(database);
        mDocReference = reference;
        mFirebaseDocumentQuery = new FirebaseDocumentQuery(database, reference);
    }

    @Override
    public CollectionQuery collection(String collection) {
        return mFirebaseDocumentQuery.collection(collection);
    }

    @Override
    public void exists(@NonNull OnQueryCompleteCallback<Boolean> callback) {

    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback) {

    }


    @Override
    public <T> void set(@NonNull T object, @NonNull OnQueryCompleteCallback<Void> callback) {
        Event event = (Event) object;
        EventSaver<ch.epfl.sdp.Event> eventSaver = new EventSaver();
//        eventSaver.saveFile(event, mDocReference.getId(), event.getDate());
    }

    @Override
    public void update(@NonNull String field, @NonNull Object value, @NonNull OnQueryCompleteCallback<Void> callback) {

    }

    @Override
    public <T> LiveData<T> livedata(@NonNull Class<T> type) {
        return null;
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {

    }
}
