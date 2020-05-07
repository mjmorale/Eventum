package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.maps.model.Marker;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Date;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapMarkerInfoWindowViewTest {

    private static final String TITLE = "title";
    private static final String DATE = "date";
    private static final String DESCRIPTION = "description";

    @Mock
    private Context mContext;

    @Mock
    private MapViewModel mMapViewModel;

    @Mock
    private LayoutInflater mInflater;

    @Mock
    private View mMarkerView;

    @Mock
    private Event mEvent;

    @Mock
    private TextView mTitleView;

    @Mock
    private TextView mDateView;

    @Mock
    private TextView mDescriptionView;

    @Mock
    private Marker mMarker;

    @Mock
    private Date mDate;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MapMarkerInfoWindowView_ConstructorTest() {
        when(mInflater.inflate(R.layout.view_map_marker_info_window, null)).thenReturn(mMarkerView);
        new MapMarkerInfoWindowView(mMapViewModel, mContext, mInflater);
        verify(mInflater).inflate(R.layout.view_map_marker_info_window, null);
    }

    @Test
    public void MapMarkerInfoWindowView_getInfoWindowTest() {
        when(mInflater.inflate(R.layout.view_map_marker_info_window, null)).thenReturn(mMarkerView);
        MapMarkerInfoWindowView view = new MapMarkerInfoWindowView(mMapViewModel, mContext, mInflater);

        when(mMapViewModel.getEventFromMarker(mMarker)).thenReturn(mEvent);
        when(mMarkerView.findViewById(anyInt())).thenReturn(mTitleView);
        when(mMarkerView.findViewById(R.id.map_event_date)).thenReturn(mDateView);
        when(mMarkerView.findViewById(R.id.map_event_description)).thenReturn(mDescriptionView);

        when(mEvent.getTitle()).thenReturn(TITLE);
        when(mEvent.getDateStr()).thenReturn(DATE);
        when(mEvent.getDescription()).thenReturn(DESCRIPTION);

        view.getInfoWindow(mMarker);
        verify(mMapViewModel).getEventFromMarker(mMarker);

        verify(mTitleView).setText(TITLE);
        verify(mDateView).setText(DATE);
        verify(mDescriptionView).setText(DESCRIPTION);
    }
}
