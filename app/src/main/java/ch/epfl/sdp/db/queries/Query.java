package ch.epfl.sdp.db.queries;

public interface Query   {

    interface OnQueryCompleteCallback<T> {
        void onGetQueryComplete(QueryResult<T> result);
    }

}
