package ch.epfl.sdp.ui.main.swipe;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.bumptech.glide.Glide;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;

import ch.epfl.sdp.db.DatabaseObject;

/**
 * Adapter to fill the swipe cards with events
 */
public class CardArrayAdapter extends ArrayAdapter<DatabaseObject<Event>> {

    /**
     * Constructor of the CardArrayAdapter
     *
     * @param context the environment the application is currently running in
     */
    public CardArrayAdapter(Context context){
        super(context, R.layout.cardview_swipe_item);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position).getObject();
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.cardview_swipe_item, parent, false);
        }

        TextView name = convertView.findViewById(R.id.eventName);
        ImageView imageView = convertView.findViewById(R.id.imageCard);
        TextView description = convertView.findViewById(R.id.eventDescription);

        name.setText(event.getTitle());

        ImageGetter.getInstance().getImage(getContext(), event.getImageId(), imageView);

        description.setText(event.getDescription());
        return convertView;
    }

}
