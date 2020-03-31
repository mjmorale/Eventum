package ch.epfl.sdp;

import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ObjectUtilsTest {

    private final static Object DUMMY_NULL = null;
    private final static String DUMMY_STRING = "test";

    @Test (expected = IllegalArgumentException.class)
    public void ObjectUtils_VerifyNotNull_ThrowsIfArgumentIsNull() {
        ObjectUtils.verifyNotNull(DUMMY_NULL);
    }

    @Test
    public void ObjectUtils_VerifyNotNull_ReturnsTheObjectWhenNotNull() {
        assertEquals(DUMMY_STRING, ObjectUtils.verifyNotNull(DUMMY_STRING));
    }

    @Test (expected = IllegalArgumentException.class)
    public void ObjectUtils_VerifyNotNull_ThrowsIfOneOfTheArgumentIsNull() {
        ObjectUtils.verifyNotNull(DUMMY_STRING, DUMMY_NULL);
    }

    @Test
    public void ObjectUtils_VerifyNotNull_DoesNotThrowIfAllArgumentsAreValid() {
        ObjectUtils.verifyNotNull(DUMMY_STRING, DUMMY_STRING);
    }
}
