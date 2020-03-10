package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import com.google.firebase.firestore.DocumentReference;

import androidx.annotation.NonNull;

public class FirebaseDocumentLiveData<TType> extends FirebaseLiveData<TType, TType> {

    private final DocumentReference mDocument;

    FirebaseDocumentLiveData(@NonNull DocumentReference document, @NonNull Class<TType> type) {
        super(type);
        if(document == null) {
            throw new IllegalArgumentException();
        }
        mDocument = document;
    }

    @Override
    protected void onActive() {
        super.onActive();

        setListener(mDocument.addSnapshotListener((documentSnapshot, e) -> {
            if(e == null) {
                if(documentSnapshot.getData() != null) {
                    postValue(mBuilder.buildFromMap(documentSnapshot.getData()));
                }
            }
            else {
                Log.e("FirestoreLiveData", "Exception during update", e);
            }
        }));
    }
}
