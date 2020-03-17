package ch.epfl.sdp.db;

import androidx.annotation.NonNull;

import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.fail;

public class DatabaseObjectBuilderTest {

    private static class MockBuilder extends DatabaseObjectBuilder<String> {

        public MockBuilder(String... required) {
            super(required);
        }

        @Override
        public String buildFromMap(@NonNull Map<java.lang.String, Object> data) {
            if(data == null) {
                throw new IllegalArgumentException();
            }
            checkRequiredFields(data);
            return (String) data.get("mock1");
        }

        @Override
        public Map<java.lang.String, Object> serializeToMap(@NonNull String object) {
            if(object == null) {
                throw new IllegalArgumentException();
            }
            return new HashMap<String, Object>() {{
                put("mock1", object);
                put("mock2", "test2");
            }};
        }
    }

    @Test (expected = IllegalArgumentException.class)
    public void DatabaseObjectBuilder_RequiredFields_FailsIfNotContained() {
        MockBuilder builder = new MockBuilder("mock1", "mock2");
        Map<String, Object> data = new HashMap<>();
        data.put("mock1", new Object());
        builder.checkRequiredFields(data);
    }

    @Test
    public void DatabaseObjectBuilder_RequiredFields_DoesNotFailIfContained() {
        MockBuilder builder = new MockBuilder("mock1", "mock2");
        Map<String, Object> data = new HashMap<String, Object>() {{
            put("mock1", new Object());
            put("mock2", new Object());
        }};
        try {
            builder.checkRequiredFields(data);
        } catch(Exception e) {
            fail();
        }
    }
}
