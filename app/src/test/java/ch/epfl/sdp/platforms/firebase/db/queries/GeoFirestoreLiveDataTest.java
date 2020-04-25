package ch.epfl.sdp.platforms.firebase.db.queries;

import com.google.firebase.Timestamp;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.GeoPoint;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.imperiumlabs.geofirestore.listeners.GeoQueryDataEventListener;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import java.util.HashMap;

import ch.epfl.sdp.Event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GeoFirestoreLiveDataTest {

    @Mock
    private GeoQuery mGeoQuery;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GeoFirestoreLiveData_Constructor_FailsWithNullGeoQueryArgument(){
        GeoFirestoreLiveData geoFirestoreLiveData = new GeoFirestoreLiveData<>(null, String.class);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GeoFirestoreLiveData_Constructor_FailsWithNullClassArgument(){
        GeoFirestoreLiveData geoFirestoreLiveData = new GeoFirestoreLiveData<>(mGeoQuery, null);
    }
}
