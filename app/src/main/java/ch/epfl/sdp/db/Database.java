package ch.epfl.sdp.db;

import androidx.annotation.NonNull;

import ch.epfl.sdp.db.queries.CollectionQuery;

public interface Database {

    CollectionQuery query(@NonNull String collection);
}
