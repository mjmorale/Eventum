package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.location.Location;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import java.util.Locale;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;

import ch.epfl.sdp.db.DatabaseObject;

/**
 * Adapter to fill the swipe cards with events
 */
public class CardArrayAdapter extends ArrayAdapter<DatabaseObject<Event>> {

    private LocationService mLocationService;

    /**
     * Constructor of the CardArrayAdapter
     *
     * @param context the environment the application is currently running in
     */
    public CardArrayAdapter(Context context, LocationService locationService){
        super(context, R.layout.cardview_swipe_item);
        mLocationService = locationService;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position).getObject();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview_swipe_item, parent, false);
        }

        float distanceInMeters = mLocationService.distanceTo(getContext(), event.getLocation());
        String distanceString = distanceInMeters < 1000.0f ?
                String.format(Locale.getDefault(), ", %dm", (int)distanceInMeters) :
                String.format(Locale.getDefault(), ", %.1fkm", distanceInMeters / 1000.0f);

        TextView name = convertView.findViewById(R.id.eventName);
        ImageView imageView = convertView.findViewById(R.id.imageCard);
        TextView description = convertView.findViewById(R.id.eventDescription);
        TextView distance = convertView.findViewById(R.id.eventDistance);

        name.setText(event.getTitle());
        distance.setText(distanceString);

        ImageGetter.getInstance().getImage(getContext(), event.getImageId(), imageView);

        description.setText(event.getDescription());
        return convertView;
    }

}
