package ch.epfl.sdp.platforms.firebase.db.queries;

import android.util.Log;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObject;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseQueryLiveData<TType> extends FirebaseLiveData<TType, List<DatabaseObject<TType>>> {

    private final Query mQuery;

    FirebaseQueryLiveData(@NonNull Query query, @NonNull Class<TType> type) {
        super(type);

        mQuery = verifyNotNull(query);
    }

    @Override
    protected void onActive() {
        super.onActive();

        setListener(mQuery.addSnapshotListener((queryDocumentSnapshots, e) -> {
            if(e == null) {
                if(queryDocumentSnapshots != null) {
                    List<DatabaseObject<TType>> data = new ArrayList<>();
                    for(DocumentSnapshot doc : queryDocumentSnapshots.getDocuments()) {
                        data.add(new DatabaseObject<>(doc.getId(), mBuilder.buildFromMap(doc.getData())));
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