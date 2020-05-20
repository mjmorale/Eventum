package ch.epfl.sdp.ui.main.map;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.VisibleForTesting;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.ImageViewModel;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View for displaying an information window on a marker click on the map
 */
public class MapMarkerInfoWindowView implements GoogleMap.InfoWindowAdapter {

    private View mMarkerView;
    private final MapViewModel mMapViewModel;
    private final ImageViewModel mImageViewModel;

    /**
     * Construct a new MapMarkerInfoWindowView
     *
     * @param context The current application context
     * @param mapViewModel The {@link MapViewModel} that handles the map that this view is displayed on.
     * @param imageViewModel The {@link ImageViewModel} that handles the image loading.
     */
    public MapMarkerInfoWindowView(@NonNull Context context, @NonNull MapViewModel mapViewModel, @NonNull ImageViewModel imageViewModel) {
        verifyNotNull(context, mapViewModel, imageViewModel);

        mMarkerView = mMarkerView.inflate(context, R.layout.view_map_marker_info_window, null);
        mMapViewModel = mapViewModel;
        mImageViewModel = imageViewModel;
    }

    /**
     * Construct a new MapMarkerInfoWindowView, for testing only.
     *
     * @param mapViewModel The injected MapViewModel
     * @param imageViewModel The injected ImageViewModel
     * @param view The injected View
     */
    @VisibleForTesting
    public MapMarkerInfoWindowView(@NonNull MapViewModel mapViewModel, @NonNull ImageViewModel imageViewModel, @NonNull View view) {
        verifyNotNull(mapViewModel, imageViewModel, view);
        mMarkerView = view;
        mMapViewModel = mapViewModel;
        mImageViewModel = imageViewModel;
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
        mImageViewModel.loadInto("events", event.getImageId(), image);

        return mMarkerView;
    }

    @Override
    public View getInfoContents(Marker marker) {
        return null;
    }
}
