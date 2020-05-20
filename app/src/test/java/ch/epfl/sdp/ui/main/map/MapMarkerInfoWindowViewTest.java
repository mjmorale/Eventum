package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.EventBuilder;
import ch.epfl.sdp.R;
import ch.epfl.sdp.offline.ImageCache;
import ch.epfl.sdp.ui.ImageViewModel;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapMarkerInfoWindowViewTest {

    String title = "Real Fake Event";
    String description = "This is really happening";
    Date date = new Date(2020,11,10);
    String dateStr =  "10/12/3920 00:00";
    LatLng location = new LatLng(100,100);
    String address = "Lausanne, Switzerland";
    String imageId = "URL";
    String organizerRef = "organizerref";

    @Mock
    private MapViewModel mMapViewModel;

    @Mock
    private ImageViewModel mImageViewModel;

    @Mock
    private View mMarkerView;

    @Mock
    private TextView mTitleView;

    @Mock
    private TextView mDateView;

    @Mock
    private TextView mDescriptionView;

    @Mock
    private ImageView mImageView;

    @Mock
    private Marker mMarker;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MapMarkerInfoWindowView_getInfoWindowTest() {
        MapMarkerInfoWindowView view = new MapMarkerInfoWindowView(mMapViewModel, mImageViewModel, mMarkerView);

        EventBuilder eventBuilder = new EventBuilder();
        Event event = eventBuilder.setTitle(title)
                .setDescription(description)
                .setDate(date)
                .setAddress(address)
                .setLocation(location)
                .setImageId(imageId)
                .setOrganizerRef(organizerRef)
                .build();

        when(mMapViewModel.getEventFromMarker(mMarker)).thenReturn(event);
        when(mMarkerView.findViewById(anyInt())).thenReturn(mTitleView);
        when(mMarkerView.findViewById(R.id.map_event_date)).thenReturn(mDateView);
        when(mMarkerView.findViewById(R.id.map_event_description)).thenReturn(mDescriptionView);
        when(mMarkerView.findViewById(R.id.map_event_image)).thenReturn(mImageView);

        view.getInfoWindow(mMarker);
        verify(mMapViewModel).getEventFromMarker(mMarker);

        verify(mTitleView).setText(title);
        verify(mDateView).setText(dateStr);
        verify(mDescriptionView).setText(description);
        verify(mImageViewModel).loadInto("events", imageId, mImageView);
    }
}
