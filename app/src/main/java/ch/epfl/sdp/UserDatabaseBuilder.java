package ch.epfl.sdp;

import java.util.HashMap;
import java.util.Map;

import androidx.annotation.NonNull;
import ch.epfl.sdp.db.DatabaseObjectBuilder;

public class UserDatabaseBuilder extends DatabaseObjectBuilder<User> {

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
}
