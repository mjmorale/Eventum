package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.VisibleForTesting;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;

/**
 * View for displaying an information window on a marker click on the map
 */
public class MapMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {

    private View mMarkerView;
    private MapViewModel mMapViewModel;
    private Context mContext;
    private ImageGetter mImageGetter;

    /**
     * Constructor of the MapMarkerInfoWindowView
     *
     * @param mapViewModel the view model of the map where the markers are
     * @param context the environment the application is currently running in
     */
    public MapMarkerInfoWindowView(MapViewModel mapViewModel, Context context) {
        mMapViewModel = mapViewModel;
        mContext = context;
        mMarkerView = mMarkerView.inflate(mContext, R.layout.view_map_marker_info_window, null);
        mImageGetter = ImageGetter.getInstance();
    }

    /**
     * Constructor of the MapMarkerInfoWindowView, only for testing purpose!
     *
     * @param mapViewModel the view model of the map where the markers are
     * @param context the environment the application is currently running in
     * @param view for testing
     * @param imageGetter for testing
     */
    @VisibleForTesting
    public MapMarkerInfoWindowView(MapViewModel mapViewModel, Context context,
                                   View view, ImageGetter imageGetter) {
        mMapViewModel = mapViewModel;
        mContext = context;
        mMarkerView = view;
        mImageGetter = imageGetter;
    }

    @Override
    public View getInfoWindow(Marker marker) {
        Event event = mMapViewModel.getEventFromMarker(marker);
        TextView title = mMarkerView.findViewById(R.id.map_event_title);
        TextView date = mMarkerView.findViewById(R.id.map_event_date);
        TextView description = mMarkerView.findViewById(R.id.map_event_description);
        ImageView image = mMarkerView.findViewById(R.id.map_event_image);
        title.setText(event.getTitle());
        date.setText(event.getDateStr());
        description.setText(event.getDescription());
        mImageGetter.getImage(mContext, event.getImageId(), image);

        return mMarkerView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
