package ch.epfl.sdp;

public class ObjectUtils {

    public static <T> T verifyNotNull(T object) {
        if(object == null) {
            throw new IllegalArgumentException("Object reference cannot be null");
        }
        return object;
    }

    public static void verifyNotNull(Object... objects) {
        for(Object object: objects) {
            verifyNotNull(object);
        }
    }
}
