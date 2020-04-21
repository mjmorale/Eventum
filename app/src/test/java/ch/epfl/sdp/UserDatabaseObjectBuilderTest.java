package ch.epfl.sdp;

import org.junit.Test;

import java.util.Map;

import ch.epfl.sdp.db.DatabaseObjectBuilderRegistry;

import static org.junit.Assert.assertEquals;

public class UserDatabaseObjectBuilderTest {

    @Test
    public void UserDatabaseObjectBuilder_CheckSymmetry() {

        User user = new User("testname", "testmail");

        Map<String, Object> data = DatabaseObjectBuilderRegistry.getBuilder(User.class).serializeToMap(user);
        User resultUser = DatabaseObjectBuilderRegistry.getBuilder(User.class).buildFromMap(data);

        assertEquals("testname", user.getName());
        assertEquals("testmail", user.getEmail());
    }
}
