package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.firestore.GeoPoint;

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

    public static GeoPoint toGeoPoint(LatLng latLng){
        return new GeoPoint(latLng.latitude, latLng.longitude);
    }
}
