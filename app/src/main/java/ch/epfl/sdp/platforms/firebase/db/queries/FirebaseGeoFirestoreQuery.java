package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.TaskCompletionSource;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoFirestore;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import androidx.annotation.Nullable;
import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.LocationQuery;
import ch.epfl.sdp.future.Future;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseGeoFirestoreQuery extends FirebaseQuery implements LocationQuery {

    protected GeoFirestore mGeoFirestore;

    private final GeoPoint mLocation;
    private final double mRadius;

    FirebaseGeoFirestoreQuery(@NonNull FirebaseFirestore database, @NonNull GeoFirestore geoFirestore, @NonNull GeoPoint location, double radius) {
        super(database);
        mGeoFirestore = verifyNotNull(geoFirestore);
        mLocation = verifyNotNull(location);
        mRadius = radius;
    }

    @Override
    public <T> Future<List<T>> get(@NonNull Class<T> type) {
        verifyNotNull(type);

        final TaskCompletionSource<List<T>> geoTask = new TaskCompletionSource<>();
        mGeoFirestore.getAtLocation(mLocation, mRadius, (documents, e) -> {
            if(e == null) {
                DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder(type);
                List<T> data = new ArrayList<>();
                for(DocumentSnapshot doc: documents) {
                    data.add(builder.buildFromMap(doc.getData()));
                }
                geoTask.setResult(data);
            }
            else {
                geoTask.setException(e);
            }
        });
        return new Future<>(geoTask.getTask());
    }

    @Override
    public <T> LiveData<List<T>> liveData(@NonNull Class<T> type) {
        return new GeoFirestoreLiveData<>(mGeoFirestore.queryAtLocation(mLocation, mRadius), verifyNotNull(type));
    }

}
