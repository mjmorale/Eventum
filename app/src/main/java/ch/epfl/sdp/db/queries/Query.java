package ch.epfl.sdp.db.queries;

/**
 * Asynchronous query interface based on a callback model.
 */
public interface Query   {

    /**
     *
     *
     * @param <T> returned object type once the query is completed
     */
    interface OnQueryCompleteCallback<T> {
        void onQueryComplete(QueryResult<T> result);
    }

}
