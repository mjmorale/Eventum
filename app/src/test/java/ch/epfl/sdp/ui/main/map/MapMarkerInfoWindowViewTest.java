package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class MapMarkerInfoWindowViewTest {

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
    private ImageView mImageView;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void MapMarkerInfoWindowView_ConstructorTest() {
        when(mInflater.inflate(any(), any())).thenReturn(mMarkerView);
        new MapMarkerInfoWindowView(mMapViewModel, mContext, mInflater);
        verify(mInflater).inflate(R.layout.view_map_marker_info_window, null);
    }




}
