package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.GeoPoint;

import java.util.List;

import ch.epfl.sdp.platforms.firebase.db.queries.FirebaseGeoFirestoreQuery;

public interface GeoFirestoreQuery extends  Query{


    FirebaseGeoFirestoreQuery setLocation(@NonNull GeoPoint geoPoint);

    FirebaseGeoFirestoreQuery setRadius(double radius);

    <T> void get(@NonNull Class<T> type, @NonNull OnQueryCompleteCallback<List<T>> callback);

}
