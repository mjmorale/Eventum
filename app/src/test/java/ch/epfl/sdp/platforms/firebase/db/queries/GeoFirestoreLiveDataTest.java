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

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.epfl.sdp.Event;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;
import static org.mockito.Mockito.doCallRealMethod;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class GeoFirestoreLiveDataTest {

    private static final Class mClass = Event.class;

    @Mock
    private GeoQuery mGeoQuery;

    @Mock
    private DocumentSnapshot mDocumentSnapshot;

    @Mock
    private GeoPoint mGeoPoint;

    @Mock
    private Exception mException;

    @Mock
    private HashMap mData;

    @Before
    public void setup(){
        MockitoAnnotations.initMocks(this);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GeoFirestoreLiveData_Constructor_failsOnNullGeoQueryArgument(){
        GeoFirestoreLiveData geoFirestoreLiveData = new GeoFirestoreLiveData(null, mClass);
    }

    @Test(expected = IllegalArgumentException.class)
    public void GeoFirestoreLiveData_Constructor_failsOnNullClassArgument(){
        GeoFirestoreLiveData geoFirestoreLiveData = new GeoFirestoreLiveData(mGeoQuery, null);
    }

    @Test
    public void GeoFirestoreLiveData_OnInactive_CallsRemoveAllListeners(){
        GeoFirestoreLiveData geoFirestoreLiveData = new GeoFirestoreLiveData(mGeoQuery, mClass);
        geoFirestoreLiveData.onInactive();

        verify(mGeoQuery).removeAllListeners();
        verifyNoMoreInteractions(mGeoQuery);
    }

    @Test
    public void GeoFirestoreLiveData_OnActive_MethodsWork(){
        doAnswer(invocation -> {
                GeoQueryDataEventListener obj = invocation.getArgument(0);
                obj.onDocumentChanged(mDocumentSnapshot, mGeoPoint);
                obj.onDocumentEntered(mDocumentSnapshot, mGeoPoint);
                obj.onDocumentExited(mDocumentSnapshot);
                obj.onDocumentMoved(mDocumentSnapshot, mGeoPoint);
                obj.onGeoQueryError(mException);
                obj.onGeoQueryReady();
            return null;
        }).when(mGeoQuery).addGeoQueryDataEventListener(any());

        when(mDocumentSnapshot.getData()).thenReturn(
                new HashMap<String, Object>(){{
                    this.put("title", "title");
                    this.put("description", "description");
                    this.put("date", new Timestamp(20, 20));
                    this.put("address", "Chemin");
                    this.put("location", new GeoPoint(64, 65));
                }}
        );

        GeoFirestoreLiveData geoFirestoreLiveData = new GeoFirestoreLiveData(mGeoQuery, mClass);
        geoFirestoreLiveData.setmData(mData);
        geoFirestoreLiveData.onActive();

        verify(mData, times(2)).put(any(),any());
        verify(mData, times(2)).remove(any());

    }


}
