package ch.epfl.sdp.firebase.db.queries;

import com.google.firebase.firestore.ListenerRegistration;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import ch.epfl.sdp.db.DatabaseObjectBuilder;
import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;

public abstract class FirebaseLiveData<TType, TData> extends LiveData<TData> {

    final DatabaseObjectBuilder<TType> mBuilder;

    private ListenerRegistration mListener = null;

    FirebaseLiveData(@NonNull Class<TType> type) {
        if(type == null) {
            throw new IllegalArgumentException();
        }

        mBuilder = DatabaseObjectBuilderRegistry.getBuilder(type);
    }

    void setListener(@Nullable ListenerRegistration listener) {
        mListener = listener;
    }

    @Override
    protected void onInactive() {
        super.onInactive();

        if(mListener != null) {
            mListener.remove();
            mListener = null;
        }
    }
}
