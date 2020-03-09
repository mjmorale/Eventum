package ch.epfl.sdp.db.queries;

import androidx.annotation.NonNull;

import java.util.List;

import ch.epfl.sdp.db.DatabaseObjectBuilder;

public interface FilterQuery extends Query {

    <T, B extends DatabaseObjectBuilder<T>> void get(@NonNull B builder, @NonNull OnQueryCompleteCallback<List<T>> callback);
}
