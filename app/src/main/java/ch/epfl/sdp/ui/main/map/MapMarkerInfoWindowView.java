package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;


/**
 * View for displaying an information window on a marker click on the map
 */
public class MapMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {

    private View mMarkerView;
    private MapViewModel mMapViewModel;
    private Context mContext;

    /**
     * @param mapViewModel the view model of the map where the markers are
     * @param context the environment the application is currently running in
     */
    public MapMarkerInfoWindowView(MapViewModel mapViewModel, Context context) {

        mMapViewModel = mapViewModel;
        mContext = context;

        LayoutInflater inflater = LayoutInflater.from(mContext);
        mMarkerView = inflater.inflate(R.layout.view_map_marker_info_window, null);
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Event event = mMapViewModel.getEventFromMarker(marker);
        TextView title = mMarkerView.findViewById(R.id.map_event_title);
        TextView date = mMarkerView.findViewById(R.id.map_event_date);
        TextView description = mMarkerView.findViewById(R.id.map_event_description);
        ImageView image = mMarkerView.findViewById(R.id.map_event_image);
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        date.setText(event.getDate().toString());
        Glide.with(mContext).load(event.getImageId()).into(image);

        return mMarkerView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
