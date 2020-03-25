package ch.epfl.sdp.db;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.utils.MockStringBuilder;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertSame;

public class DatabaseObjectBuilderRegistryTest {

    @Test
    public void databaseObjectBuilderRegistry_static_HasDefaultEventBuilder() {
        assertNotNull(DatabaseObjectBuilderRegistry.getBuilder(Event.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_GetBuilder_FailsIfDoesNotContainBuilder() {
        assertNull(DatabaseObjectBuilderRegistry.getBuilder(Object.class));
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_GetBuilder_FailsWithNullClassParameter() {
        DatabaseObjectBuilderRegistry.getBuilder(null);
    }

    @Test
    public void databaseObjectBuilderFactory_GetBuilder_ReturnsInstanceOfTheCorrectClass() {
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
        MockStringBuilder res = DatabaseObjectBuilderRegistry.getBuilder(String.class);
        assertEquals(MockStringBuilder.class, res.getClass());
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_RegisterBuilder_FailsWithNullObjectClassParameter() {
        DatabaseObjectBuilderRegistry.registerBuilder(null, MockStringBuilder.class);
    }

    @Test (expected = IllegalArgumentException.class)
    public void databaseObjectBuilderFactory_RegisterBuilder_FailsWithNullBuilderClassParameter() {
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, null);
    }

    @Test
    public void databaseObjectBuilderFactory_RegisterBuilder_DoesNotCreateNewBuilderIfAlreadyPresent() {
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
        MockStringBuilder builder = DatabaseObjectBuilderRegistry.getBuilder(String.class);
        DatabaseObjectBuilderRegistry.registerBuilder(String.class, MockStringBuilder.class);
        assertSame(builder, DatabaseObjectBuilderRegistry.getBuilder(String.class));
    }
}
