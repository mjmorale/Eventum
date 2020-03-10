package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.ListenerRegistration;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;

public class FirebaseDocumentLiveData<T, B extends DatabaseObjectBuilder<T>> extends LiveData<T> {

    private final DocumentReference mDocument;
    private final B mBuilder;

    private ListenerRegistration mListener = null;

    FirebaseDocumentLiveData(@NonNull DocumentReference document, @NonNull Class<T> type) {
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
