package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;

public class FirebaseQueryLiveData<T, B extends DatabaseObjectBuilder<T>> extends LiveData<List<T>> {

    private final Query mQuery;
    private final B mBuilder;

    private ListenerRegistration mListener = null;

    FirebaseQueryLiveData(@NonNull Query query, @NonNull Class<T> type) {
        if(query == null || type == null) {
            throw new IllegalArgumentException();
        }
        mQuery = query;
        mBuilder = DatabaseObjectBuilderFactory.getBuilder(type);
    }

    @Override
    protected void onActive() {
        super.onActive();

        mListener = mQuery.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(e == null) {
                if(queryDocumentSnapshots != null) {
                    List<T> data = new ArrayList<>();
                    for(DocumentSnapshot doc: queryDocumentSnapshots) {
                        data.add(mBuilder.buildFromMap(doc.getData()));
                    }
                    postValue(data);
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