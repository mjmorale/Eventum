package ch.epfl.sdp.platforms.firebase.db.queries;

import org.imperiumlabs.geofirestore.GeoQuery;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.runners.MockitoJUnitRunner;

import ch.epfl.sdp.Event;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;

@RunWith(MockitoJUnitRunner.class)
public class GeoFirestoreLiveDataTest {

    private static final Class mClass = Event.class;

    @Mock
    private GeoQuery mGeoQuery;

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


}
