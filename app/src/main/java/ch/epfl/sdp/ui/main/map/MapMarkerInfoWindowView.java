package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

// ADD JAVADOC !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
public class MapMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {

    private View mMarkerView;
    private MapViewModel mMapViewModel;
    private Context mContext;

    public MapMarkerInfoWindowView(MapViewModel mapViewModel, Context context) {

        mMapViewModel = mapViewModel;
        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mMarkerView = inflater.inflate(R.layout.view_map_marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Event event = mMapViewModel.getEventFromMarker(marker);
        TextView description = mMarkerView.findViewById(R.id.map_event_description);
        description.setText(event.getDescription());

        return mMarkerView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
