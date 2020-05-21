package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.Locale;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.map.LocationService;
import ch.epfl.sdp.offline.ImageCache;

import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.storage.Storage;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Adapter to fill the swipe cards with events
 */
public class CardArrayAdapter extends ArrayAdapter<DatabaseObject<Event>> {

    private static final String TAG = "CardArrayAdapter";

    private final LocationService mLocationService;
    private final Storage mStorage;

    /**
     * Constructor of the CardArrayAdapter
     *
     * @param context the environment the application is currently running in
     */
    public CardArrayAdapter(@NonNull Context context, @NonNull LocationService locationService, @NonNull Storage storage) {
        super(verifyNotNull(context), R.layout.cardview_swipe_item);
        mLocationService = verifyNotNull(locationService);
        mStorage = verifyNotNull(storage);
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
                String.format(Locale.getDefault(), "%d m", (int)distanceInMeters) :
                String.format(Locale.getDefault(), "%d km", (int) distanceInMeters / 1000);

        TextView name = convertView.findViewById(R.id.eventName);
        ImageView imageView = convertView.findViewById(R.id.imageCard);
        TextView description = convertView.findViewById(R.id.eventDescription);
        TextView distance = convertView.findViewById(R.id.eventDistance);

        name.setText(event.getTitle());
        description.setText(event.getDescription());
        distance.setText(distanceString);

        mStorage.downloadImage(getContext().getCacheDir(), "events", event.getImageId(), 1, new Storage.BitmapReadyCallback() {
            @Override
            public void onSuccess(Bitmap bitmap) {
                imageView.setImageBitmap(bitmap);
            }

            @Override
            public void onFailure(Exception e) {
                Log.e(TAG, "Cannot download image from database", e);
            }
        });
        return convertView;
    }

}
