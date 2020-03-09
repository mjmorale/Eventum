package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class FirebaseFilterQuery extends FirebaseQuery implements FilterQuery {

    private final Query mQuery;

    FirebaseFilterQuery(@NonNull FirebaseFirestore database, @NonNull Query query) {
        super(database);
        if(query == null) {
            throw new IllegalArgumentException();
        }
        mQuery = query;
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        if(type == null || callback == null) {
            throw new IllegalArgumentException();
        }
        mQuery.get().addOnCompleteListener(task -> {
            if(task.isSuccessful()) {
                DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderFactory.getBuilder(type);
                List<T> data = new ArrayList<>();
                for(DocumentSnapshot doc: task.getResult()) {
                    data.add(builder.buildFromMap(doc.getData()));
                }
                callback.onGetQueryComplete(QueryResult.success(data));
            }
            else {
                callback.onGetQueryComplete(QueryResult.failure(task.getException()));
            }
        });
    }

    @Override
    public <T> LiveData<List<T>> livedata(@NonNull Class<T> type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }
        return new FirebaseQueryLiveData(mQuery, type);
    }

    private class FirebaseQueryLiveData<T, B extends DatabaseObjectBuilder<T>> extends LiveData<List<T>> {

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
}
