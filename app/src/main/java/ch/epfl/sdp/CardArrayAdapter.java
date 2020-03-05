package ch.epfl.sdp;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import java.util.List;

import ch.epfl.sdp.databinding.EventcardBinding;

public class CardArrayAdapter extends ArrayAdapter<Event> {
    EventcardBinding binding;
    public CardArrayAdapter(Context context, int resourceID, List<Event> items){
        super(context, resourceID, items);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        Event event = getItem(position);
        if(convertView == null){
            convertView = LayoutInflater.from(getContext()).inflate(R.layout.eventcard, parent, false);
        }

        TextView name = convertView.findViewById(R.id.eventName);
        ImageView imageView = convertView.findViewById(R.id.imageCard);

        name.setText(event.getTitle());
        imageView.setImageResource(event.getImageID());

        return convertView;
    }
}
