package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.GeoPoint;
import com.google.firebase.firestore.Query;

import org.imperiumlabs.geofirestore.GeoQuery;

import java.util.List;

import ch.epfl.sdp.db.queries.GeoFirestoreQuery;

public class FirebaseGeoFirestoreQuery extends FirebaseQuery implements GeoFirestoreQuery {

    private GeoQuery mQuery;

    FirebaseGeoFirestoreQuery(@NonNull FirebaseFirestore firebaseFirestore, GeoQuery geoQuery){
        super(firebaseFirestore);
        mQuery = geoQuery;
    }

    @Override
    public FirebaseGeoFirestoreQuery setLocation(@NonNull GeoPoint geoPoint) {
        mQuery.setCenter(geoPoint);
        return this;
    }

    @Override
    public FirebaseGeoFirestoreQuery setRadius(double radius){
        mQuery.setRadius(radius);
        return this;
    }

    @Override
    public <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback) {
        if(type == null || callback == null){
            throw new IllegalArgumentException();
        }
        List<com.google.firebase.firestore.Query> queryList = mQuery.getQueries();
        for(Query query : queryList){
            handleQuerySnapshot(query.get(), type, callback);
        }
    }
}
