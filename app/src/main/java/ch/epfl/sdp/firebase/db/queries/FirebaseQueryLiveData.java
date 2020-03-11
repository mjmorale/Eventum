package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;

public class FirebaseQueryLiveData<TType> extends FirebaseLiveData<TType, List<TType>> {

    private final Query mQuery;

    FirebaseQueryLiveData(@NonNull Query query, @NonNull Class<TType> type) {
        super(type);
        if(query == null) {
            throw new IllegalArgumentException();
        }
        mQuery = query;
    }

    @Override
    protected void onActive() {
        super.onActive();

        setListener(mQuery.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(e == null) {
                if(queryDocumentSnapshots != null) {
                    List<TType> data = new ArrayList<>();
                    for(DocumentSnapshot doc: queryDocumentSnapshots) {
                        data.add(mBuilder.buildFromMap(doc.getData()));
                    }
                    postValue(data);
                }
            }
            else {
                Log.e("FirestoreLiveData", "Exception during update", e);
            }
        }));
    }
}