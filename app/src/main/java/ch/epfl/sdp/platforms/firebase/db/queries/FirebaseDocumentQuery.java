package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.future.Future;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class FirebaseDocumentQuery extends FirebaseQuery implements DocumentQuery {

    private final DocumentReference mDocument;

    FirebaseDocumentQuery(@NonNull FirebaseFirestore database, @NonNull DocumentReference document) {
        super(database);

        mDocument = verifyNotNull(document);
    }

    @Override
    public CollectionQuery collection(@NonNull String collection) {
        return new FirebaseCollectionQuery(mDb, mDocument.collection(verifyNotNull(collection)));
    }

    @Override
    public Future<Boolean> exists() {
        return new Future<>(mDocument.get().continueWith(task -> task.getResult().exists()));
    }

    @Override
    public <T> Future<T> get(@NonNull Class<T> type) {
        verifyNotNull(type);
        return handleDocumentSnapshot(mDocument.get(), type);
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> Future<Void> set(@NonNull T object) {
        verifyNotNull(object);
        DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder((Class<T>)object.getClass());
        return new Future<>(mDocument.set(builder.serializeToMap(object)));
    }

    @Override
    public Future<Void> update(@NonNull String field, @NonNull Object value) {
        verifyNotNull(field, value);
        return new Future<>(mDocument.update(field, value));
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> LiveData<T> livedata(@NonNull Class<T> type) {
        return new FirebaseDocumentLiveData(mDocument, type);
    }

    @Override
    public Future<Void> delete() {
        return new Future<>(mDocument.delete());
    }

}
