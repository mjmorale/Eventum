package ch.epfl.sdp.ui.main.swipe;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;
import ch.epfl.sdp.ui.main.attending.AttendingEventAdapter;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class CardArrayAdapter2 extends RecyclerView.Adapter<CardArrayAdapter2.CardArrayViewHolder> {

    private ArrayAdapter<Event> mAttendingEvents;

    public static class CardArrayViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public ImageView mImage;
        public TextView mDescription;

        public CardArrayViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.eventName);
            mImage = itemView.findViewById(R.id.imageCard);
            mDescription = itemView.findViewById(R.id.eventDescription);
        }
    }

    public CardArrayAdapter2(@NonNull ArrayAdapter<Event> attendingEvents) {
        mAttendingEvents = verifyNotNull(attendingEvents);
    }

    public void setCardArray(@NonNull ArrayAdapter<Event> attendingEvents) {
        mAttendingEvents = verifyNotNull(attendingEvents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public CardArrayAdapter2.CardArrayViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_swipe_item, parent, false);
        CardArrayAdapter2.CardArrayViewHolder cardArrayViewHolder = new CardArrayAdapter2.CardArrayViewHolder(v);
        return cardArrayViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull CardArrayAdapter2.CardArrayViewHolder holder, int position) {
        Event event = mAttendingEvents.getItem(position);
        holder.mTitle.setText(event.getTitle());
        holder.mImage.setImageResource(event.getImageID());
        holder.mDescription.setText(event.getDescription());
    }

    @Override
    public int getItemCount() {
        return mAttendingEvents.getCount();
    }
}
