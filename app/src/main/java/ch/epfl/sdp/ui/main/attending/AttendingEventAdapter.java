package ch.epfl.sdp.ui.main.attending;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendingEventAdapter extends RecyclerView.Adapter<AttendingEventAdapter.AttendingEventViewHolder> {

    private List<Event> mAttendingEvents;

    public static class AttendingEventViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;

        public AttendingEventViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.attending_event_title);
        }
    }

    public AttendingEventAdapter(@NonNull List<Event> attendingEvents) {
        mAttendingEvents = verifyNotNull(attendingEvents);
    }

    public void setAttendingEvents(@NonNull List<Event> attendingEvents) {
        mAttendingEvents = verifyNotNull(attendingEvents);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public AttendingEventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_attending_item, parent, false);
        AttendingEventViewHolder attendingEventViewHolder = new AttendingEventViewHolder(v);
        return attendingEventViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendingEventViewHolder holder, int position) {
        Event event = mAttendingEvents.get(position);
        holder.mTitle.setText(event.getTitle());
    }

    @Override
    public int getItemCount() {
        return mAttendingEvents.size();
    }
}
