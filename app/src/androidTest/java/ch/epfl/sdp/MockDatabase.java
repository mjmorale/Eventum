package ch.epfl.sdp;

import androidx.annotation.NonNull;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;

public class MockDatabase implements Database {
    @Override
    public CollectionQuery query(@NonNull String collection) {
        return new MockCollectionQuery();
    }
}
