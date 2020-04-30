package ch.epfl.sdp.db.queries;

/**
 * Asynchronous query interface based on a callback model.
 */
public interface Query   {

    /**
     * Callback interface for query completion
     * @param <T> The return type of the query.
     */
    interface OnQueryCompleteCallback<T> {

        /**
         * Callback method called on query completion
         * @param result The result value of the query
         */
        void onQueryComplete(QueryResult<T> result);
    }

}
