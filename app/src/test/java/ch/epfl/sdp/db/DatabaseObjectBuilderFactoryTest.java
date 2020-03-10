package ch.epfl.sdp.db;

import androidx.annotation.NonNull;

import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.fail;

public class DatabaseObjectBuilderFactoryTest {

    static class MockBuilder extends DatabaseObjectBuilder<String> {

        public MockBuilder() {
            super("mock1", "mock2");
        }

        @Override
        public String buildFromMap(@NonNull Map<String, Object> data) {
            checkRequiredFields(data);
            return (String) data.get("mock1");
        }

        @Override
        public Map<String, Object> serializeToMap(@NonNull String object) {
            Map<String, Object> result = new HashMap<>();
            result.put("mock1", "test1");
            result.put("mock2", "test2");
            return result;
        }
    }

    @Before
    public void init() {
        DatabaseObjectBuilderFactory.clear();
    }

    @Test
    public void databaseObjectBuilderFactory_GetBuilder_ReturnsNullIfDoesNotContainBuilder() {
        assertNull(DatabaseObjectBuilderFactory.getBuilder(Object.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_GetBuilder_FailsWithNullClassParameter() {
        DatabaseObjectBuilderFactory.getBuilder(null);
    }

    @Test
    public void databaseObjectBuilderFactory_GetBuilder_ReturnsInstanceOfTheCorrectClass()
            throws IllegalAccessException, InstantiationException {
        DatabaseObjectBuilderFactory.registerBuilder(String.class, MockBuilder.class);
        DatabaseObjectBuilder<String> res = DatabaseObjectBuilderFactory.getBuilder(String.class);
        assertEquals(MockBuilder.class, res.getClass());
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_RegisterBuilder_FailsWithNullObjectClassParameter()
            throws IllegalAccessException, InstantiationException {
        DatabaseObjectBuilderFactory.registerBuilder(null, MockBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_RegisterBuilder_FailsWithNullBuilderClassParameter()
            throws IllegalAccessException, InstantiationException {
        DatabaseObjectBuilderFactory.registerBuilder(String.class, null);
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_RegisterBuilder_FailsIfDuplicateObjectClass()
            throws IllegalAccessException, InstantiationException {
        try {
            DatabaseObjectBuilderFactory.registerBuilder(String.class, MockBuilder.class);
        } catch(IllegalArgumentException e) {
            fail();
        }
        DatabaseObjectBuilderFactory.registerBuilder(String.class, MockBuilder.class);
    }
}
