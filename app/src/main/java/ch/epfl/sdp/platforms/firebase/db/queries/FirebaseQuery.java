package ch.epfl.sdp.platforms.firebase.db.queries;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;
import ch.epfl.sdp.future.Future;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public abstract class FirebaseQuery {

    protected final FirebaseFirestore mDb;

    public FirebaseQuery(@NonNull FirebaseFirestore database) {
        mDb = verifyNotNull(database);
    }

    protected <T> Future<T> handleDocumentSnapshot(@NonNull Task<DocumentSnapshot> docTask, @NonNull Class<T> type) {
        return new Future<>(docTask.continueWith(task -> {
            DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder(type);
            DocumentSnapshot doc = task.getResult();
            if(doc.exists()) {
                return builder.buildFromMap(doc.getData());
            }
            else {
                throw new NullPointerException("Document does not exist");
            }
        }));
    }

    protected <T> Future<List<T>> handleQuerySnapshot(@NonNull Task<QuerySnapshot> queryTask, @NonNull Class<T> type) {
        return new Future<>(queryTask.continueWith(task -> {
            DatabaseObjectBuilder<T> builder = DatabaseObjectBuilderRegistry.getBuilder(type);
            List<DocumentSnapshot> docs = task.getResult().getDocuments();
            List<T> data = new ArrayList<>();
            for(DocumentSnapshot doc: docs) {
                data.add(builder.buildFromMap(doc.getData()));
            }
            return data;
        }));
    }
}
