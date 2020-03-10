package ch.epfl.sdp.db.queries;

public interface Query   {

    interface OnQueryCompleteCallback<T> {
        void onQueryComplete(QueryResult<T> result);
    }

}
