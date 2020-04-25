package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import androidx.lifecycle.LiveData;

import org.imperiumlabs.geofirestore.GeoFirestore;

import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.db.queries.LocationQuery;
import ch.epfl.sdp.future.Future;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseCollectionQuery extends FirebaseQuery implements CollectionQuery {

    private final CollectionReference mCollection;

    public FirebaseCollectionQuery(@NonNull FirebaseFirestore database, @NonNull CollectionReference collection) {
        super(database);

        mCollection = verifyNotNull(collection);
    }

    @Override
    public DocumentQuery document(@NonNull String document) {
        return new FirebaseDocumentQuery(mDb, mCollection.document(verifyNotNull(document)));
    }

    @Override
    public FilterQuery whereFieldEqualTo(@NonNull String field, Object value) {
        return new FirebaseFilterQuery(mDb, mCollection.whereEqualTo(verifyNotNull(field), value));
    }

    @Override
    public FilterQuery orderBy(@NonNull String field) {
        return new FirebaseFilterQuery(mDb, mCollection.orderBy(verifyNotNull(field)));
    }

    @Override
    public FilterQuery limitCount(int count) {
        if(count <= 0) {
            throw new IllegalArgumentException();
        }
        return new FirebaseFilterQuery(mDb, mCollection.limit(count));
    }

    @Override
    public LocationQuery atLocation(double latitude, double longitude, double radius) {
        return new FirebaseGeoFirestoreQuery(mDb, new GeoFirestore(mCollection), new GeoPoint(latitude, longitude), radius);
    }

    @Override
    public <T> Future<List<T>> get(@NonNull Class<T> type) {
        verifyNotNull(type);
        return handleQuerySnapshot(mCollection.get(), type);
    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return new FirebaseQueryLiveData(mCollection, verifyNotNull(type));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<String> create(@NonNull T object) {
        verifyNotNull(object);
        DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder((Class<T>) object.getClass());
        Map<String, Object> data = builder.serializeToMap(object);
        Task<DocumentReference> addTask = mCollection.add(data);
        return new Future<>(addTask.continueWithTask(task -> {
            String ref = task.getResult().getId();
            if(builder.hasLocation()) {
                GeoFirestore geoFirestore = new GeoFirestore(mCollection);
                final TaskCompletionSource<String> geoTask = new TaskCompletionSource<>();
                geoFirestore.setLocation(ref, new GeoPoint(builder.getLatitude(object), builder.getLongitude(object)), e -> {
                    if(e == null) {
                        geoTask.setResult(ref);
                    }
                    else {
                        geoTask.setException(e);
                    }
                });
                return geoTask.getTask();
            }
            return Tasks.forResult(ref);
        }));
    }
}
