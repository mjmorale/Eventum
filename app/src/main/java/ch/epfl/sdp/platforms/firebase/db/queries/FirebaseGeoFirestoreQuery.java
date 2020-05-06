package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

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
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.LocationQuery;
import ch.epfl.sdp.db.queries.Query;
import ch.epfl.sdp.db.queries.QueryResult;

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
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<DatabaseObject<T>>> callback) {
        verifyNotNull(type, callback);

        mGeoFirestore.getAtLocation(mLocation, mRadius, (list, e) -> {
            handleLocationQuerySnapshot(list, e, type, callback); }
            );
    }

    @Override
    public <T> LiveData<Collection<DatabaseObject<T>>> liveData(@NonNull Class<T> type) {
        return new GeoFirestoreLiveData<T>(mGeoFirestore.queryAtLocation(mLocation, mRadius), verifyNotNull(type));
    }

    @SuppressWarnings("unchecked")
    protected <T> void handleLocationQuerySnapshot(@Nullable List<? extends DocumentSnapshot> documents,
                                                 @Nullable Exception e,
                                                 @NonNull Class<T> type,
                                                 @NonNull Query.OnQueryCompleteCallback callback) {
        verifyNotNull(type, callback);

        if(documents == null) {
            callback.onQueryComplete(QueryResult.failure(e));
        }
        else {
            DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder(type);
            List<DatabaseObject<T>> data = new ArrayList<>();
            for(DocumentSnapshot doc: documents) {
                data.add(new DatabaseObject(doc.getId(), builder.buildFromMap(doc.getData())));
            }
            callback.onQueryComplete(QueryResult.success(data));
        }
    }

    @VisibleForTesting
    public GeoPoint getmLocation(){
        return mLocation;
    }

    @VisibleForTesting
    public double getmRadius(){
        return mRadius;
    }
}
