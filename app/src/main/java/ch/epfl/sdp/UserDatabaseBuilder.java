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
        super("username", "email");
    }

    @Override
    public User buildFromMap(@NonNull Map<String, Object> data) {
        String name = (String)data.get("username");
        String email = (String)data.get("email");
        return new User(name, email);
    }

    @Override
    public Map<String, Object> serializeToMap(@NonNull User object) {
        return new HashMap<String, Object>() {{
            put("username", object.getName());
            put("email", object.getEmail());
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
