package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class GeoFirestoreLiveData<TType> extends LiveData<Collection<TType>> {

    private final DatabaseObjectBuilder<TType> mBuilder;
    private final GeoQuery mGeoQuery;
    private final Map<String, TType> mData = new HashMap<>();

    GeoFirestoreLiveData(@NonNull GeoQuery geoQuery, @NonNull Class<TType> type) {
        verifyNotNull(geoQuery, type);

        mBuilder = DatabaseObjectBuilderRegistry.getBuilder(type);
        mGeoQuery = verifyNotNull(geoQuery);
    }

    @Override
    protected void onActive() {
        super.onActive();

        mGeoQuery.addGeoQueryDataEventListener(new GeoQueryDataEventListener() {
            @Override
            public void onDocumentEntered(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
                addDocument(documentSnapshot);
            }

            @Override
            public void onDocumentExited(DocumentSnapshot documentSnapshot) {
                removeDocument(documentSnapshot);
            }

            @Override
            public void onDocumentMoved(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) { }

            @Override
            public void onDocumentChanged(DocumentSnapshot documentSnapshot, GeoPoint geoPoint) {
                addDocument(documentSnapshot);
            }

            @Override
            public void onGeoQueryReady() { }

            @Override
            public void onGeoQueryError(Exception e) { }

        });
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        mGeoQuery.removeAllListeners();
    }

    private void addDocument(@NonNull DocumentSnapshot document) {
        verifyNotNull(document);
        mData.put(document.getId(), mBuilder.buildFromMap(document.getData()));
        postCurrentValue();
    }

    private void removeDocument(@NonNull DocumentSnapshot document) {
        verifyNotNull(document);
        mData.remove(document.getId());
        postCurrentValue();
    }

    private void postCurrentValue() {
        postValue(mData.values());
    }
}
