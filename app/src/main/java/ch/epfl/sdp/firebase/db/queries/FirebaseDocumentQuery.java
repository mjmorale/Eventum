package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class FirebaseDocumentQuery extends FirebaseQuery implements DocumentQuery {

    private final DocumentReference mDocument;

    FirebaseDocumentQuery(@NonNull FirebaseFirestore database, @NonNull DocumentReference document) {
        super(database);
        if(document == null) {
            throw new IllegalArgumentException();
        }
        mDocument = document;
    }

    @Override
    public CollectionQuery collection(@NonNull String collection) {
        if(collection == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseCollectionQuery(mDb, mDocument.collection(collection));
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<T> callback) {
        if(type == null || callback == null) {
            throw new IllegalArgumentException();
        }
        handleDocumentSnapshot(mDocument.get(), type, callback);
    }

    @Override
    public <T> LiveData<T> livedata(@NonNull Class<T> type) {
        return new FirebaseDocumentLivedata(mDocument, type);
    }

    @Override
    public void delete(@NonNull OnQueryCompleteCallback<Void> callback) {
        if(callback == null) {
            throw new IllegalArgumentException();
        }
        mDocument.delete().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                callback.onQueryComplete(QueryResult.success(null));
            } else {
                callback.onQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    private class FirebaseDocumentLivedata<T, B extends DatabaseObjectBuilder<T>> extends LiveData<T> {

        private final DocumentReference mDocument;
        private final B mBuilder;

        private ListenerRegistration mListener = null;

        FirebaseDocumentLivedata(@NonNull DocumentReference document, @NonNull Class<T> type) {
            if(document == null || type == null) {
                throw new IllegalArgumentException();
            }
            mDocument = document;
            mBuilder = DatabaseObjectBuilderFactory.getBuilder(type);
        }

        @Override
        protected void onActive() {
            super.onActive();

            mListener = mDocument.addSnapshotListener((documentSnapshot, e) -> {
                if(e == null) {
                    if(documentSnapshot.getData() != null) {
                        postValue(mBuilder.buildFromMap(documentSnapshot.getData()));
                    }
                }
                else {
                    Log.e("FirestoreLiveData", "Exception during update", e);
                }
            });
        }

        @Override
        protected void onInactive() {
            super.onInactive();

            if(mListener != null) {
                mListener.remove();
                mListener = null;
            }
        }
    }
}
