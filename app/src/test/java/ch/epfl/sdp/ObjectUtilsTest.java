package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

import org.junit.Test;

import static junit.framework.TestCase.assertTrue;
import static org.junit.Assert.assertEquals;

public class ObjectUtilsTest {

    private final static Object DUMMY_NULL = null;
    private final static String DUMMY_STRING = "test";
    private final static double LAT = 65;
    private final static double LNG = 64;

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

    @Test
    public void ObjectUtils_toGeoPoint_works(){
        GeoPoint geoPoint = ObjectUtils.toGeoPoint(new LatLng(65, 64));
        assertTrue(geoPoint.getLatitude() == 65);
        assertTrue(geoPoint.getLongitude() == 64);
    }
}
