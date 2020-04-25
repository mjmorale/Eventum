package ch.epfl.sdp;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
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
    public double getLatitude(@NonNull User object) {
        return 0;
    }

    @Override
    public double getLongitude(@NonNull User object) {
        return 0;
    }

    @Override
    public boolean hasLocation() {
        return false;
    }
}
