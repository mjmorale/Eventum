package ch.epfl.sdp.firebase.db.queries;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderFactory;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.QueryResult;

public class FirebaseFilterQuery extends FirebaseQuery implements FilterQuery {

    private Query mQuery;

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
        handleQuerySnapshot(mQuery.get(), type, callback);
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        if(field == null) {
            throw new IllegalArgumentException();
        }
        mQuery = mQuery.whereEqualTo(field, value);
        return this;
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        if(field == null) {
            throw new IllegalArgumentException();
        }
        mQuery = mQuery.orderBy(field);
        return this;
    }

    @Override
    public FilterQuery limitCount(int count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        mQuery = mQuery.limit(count);
        return this;
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
