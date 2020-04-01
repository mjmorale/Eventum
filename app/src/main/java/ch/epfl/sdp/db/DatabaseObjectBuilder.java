package ch.epfl.sdp.db;

import com.google.firebase.firestore.GeoPoint;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public abstract class DatabaseObjectBuilder<T> {

    private final List<String> mRequiredFields;
    private final boolean mHasLocation;

    protected DatabaseObjectBuilder(boolean hasLocation, String... requiredFields) {
        mHasLocation = hasLocation;
        mRequiredFields = Arrays.asList(requiredFields);
    }

    public boolean hasLocation() {
        return mHasLocation;
    }

    @Nullable
    public T buildFromMap(@NonNull Map<String, Object> data) {
        verifyNotNull(data);

        checkRequiredFields(data);
        return null;
    }

    @Nullable
    public Map<String, Object> serializeToMap(@NonNull T object) {
        verifyNotNull(object);

        return null;
    }

    @Nullable
    public GeoPoint getLocation(@NonNull T object) {
        verifyNotNull(object);

        return null;
    }

    protected void checkRequiredFields(Map<String, Object> data) {
        for(String field: mRequiredFields) {
            if(!data.containsKey(field)) {
                throw new IllegalArgumentException("Missing field in database map: " + field);
            }
        }
    }
}
