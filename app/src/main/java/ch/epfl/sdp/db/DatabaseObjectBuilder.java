package ch.epfl.sdp.db;

import com.google.android.gms.maps.model.LatLng;

import java.util.Arrays;
import java.util.List;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

/**
 * Class that can convert back and forth an object into a database compatible structure.
 *
 * @param <T> Type of the object that this builder operates on.
 */
public abstract class DatabaseObjectBuilder<T> {

    private final List<String> mRequiredFields;

    /**
     * Constructs a new DatabaseObjectBuilder.
     * 
     * @param requiredFields List of required fields.
     * @see #checkRequiredFields(Map)
     */
    protected DatabaseObjectBuilder(String... requiredFields) {
        mRequiredFields = Arrays.asList(requiredFields);
    }

    /**
     * Indicates if this object has a location parameter, i.e. geographical coordinates.
     *
     * This is required by the database interface in order to be compatible with
     * GeoFirestore.
     *
     * @return True if the object has a location parameter, false otherwise.
     * @see #getLocation(Object) 
     */
    public abstract boolean hasLocation();

    /**
     * Creates a new instance of T from a database queried structure.
     *
     * @param data The database object to convert.
     * @throws IllegalArgumentException The data object is null, or the data does not contain a
     * required argument.
     *
     * @return A new instance of T containing the queried data.
     * @see #checkRequiredFields(Map)
     */
    @Nullable
    public abstract T buildFromMap(@NonNull Map<String, Object> data);

    /**
     * Creates a valid database compatible object from an instance of T.
     *
     * @param object An instance of T to be converted.
     * @throws IllegalArgumentException The object is null.
     *
     * @return The database compatible structure.
     */
    public abstract Map<String, Object> serializeToMap(@NonNull T object);

    /**
     * Extracts the latitude parameter of an instance of T.
     * 
     * This method needs to be implemented if the object contains a location, i.e. the
     * <code>hasLocation</code> function returns <code>true</code>.
     * Otherwise, this function can safely return anything.
     *
     * @param object An instance of T to extract the location from.
     * @throws IllegalArgumentException The object is null.
     *
     * @return The latitude of the instance of T.
     * @see #hasLocation()
     */
    public abstract double getLatitude(@NonNull T object);

    /**
     * Extracts the longitude parameter of an instance of T.
     *
     * This method needs to be implemented if the object contains a location, i.e. the
     * <code>hasLocation</code> function returns <code>true</code>.
     * Otherwise, this function can safely return anything.
     *
     * @param object An instance of T to extract the location from.
     * @throws IllegalArgumentException The object is null.
     *
     * @return The longitude of the instance of T.
     * @see #hasLocation()
     */
    public abstract double getLongitude(@NonNull T object);

    /**
     * Checks that the provided data contains a specific set of arguments.
     *
     * @param data The database queried data.
     * @throws IllegalArgumentException One or more arguments are missing from the provided map.
     */
    protected void checkRequiredFields(Map<String, Object> data) {
        for(String field: mRequiredFields) {
            if(!data.containsKey(field)) {
                throw new IllegalArgumentException("Missing field in database map: " + field);
            }
        }
    }
}
