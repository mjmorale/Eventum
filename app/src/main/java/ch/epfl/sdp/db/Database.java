package ch.epfl.sdp.db;

import androidx.annotation.NonNull;

import ch.epfl.sdp.db.queries.CollectionQuery;


/**
 * The database architecture follows the Firestore model where a document stores the actual
 * information and is contained inside a collection that can regroup multiple document.
 * This is the main interface that each object should provide in order to communicate with a
 * database.
 */
public interface Database {

    /**
     * Query a collection given an id/reference.
     *
     * @param collection reference to the desired collection
     * @return query {@link ch.epfl.sdp.db.queries.CollectionQuery} object associated
     * to the reference argument
     */
    CollectionQuery query(@NonNull String collection);
}
