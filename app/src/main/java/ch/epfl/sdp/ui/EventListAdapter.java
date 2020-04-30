package ch.epfl.sdp.ui;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sdp.Event;
import ch.epfl.sdp.R;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    private List<Event> mEventList;

    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.eventlist_item_title);
        }
    }

    public EventListAdapter() {
        mEventList = new ArrayList<>();
    }

    public EventListAdapter(@NonNull List<Event> events) {
        mEventList = verifyNotNull(events);
    }

    public void clear() {
        mEventList.clear();
        notifyDataSetChanged();
    }

    public void add(@NonNull Event event) {
        mEventList.add(verifyNotNull(event));
        notifyDataSetChanged();
    }

    public void addAll(@NonNull List<Event> events) {
        mEventList.addAll(events);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public EventViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_eventlist_item, parent, false);
        EventViewHolder viewHolder = new EventViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull EventViewHolder holder, int position) {
        Event event = mEventList.get(position);
        holder.mTitle.setText(event.getTitle());
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
