package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

/**
 * Class that can convert a User instance to a database compatible object.
 * @see User
 */
public class UserDatabaseBuilder extends DatabaseObjectBuilder<User> {

    /**
     * Construct a new UserDatabaseBuilder instance.
     */
    public UserDatabaseBuilder() {
        super("username", "email","imageId", "description");
    }

    @Override
    public User buildFromMap(@NonNull Map<String, Object> data) {
        String name = (String)data.get("username");
        String email = (String)data.get("email");
        String imageId = (String)data.get("imageId");
        String decription= (String)data.get("description");
        return new User(name, email, imageId, decription);
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull User object) {
        return new HashMap<String, Object>() {{
            put("username", object.getName());
            put("email", object.getEmail());
            put("imageId", object.getImageId());
            put("description", object.getDescription());
        }};
    }

    @Override
    public boolean hasLocation() {
        return false;
    }

    @Nullable
    @Override
    public LatLng getLocation(@NonNull User object) {
        return null;
    }
}
