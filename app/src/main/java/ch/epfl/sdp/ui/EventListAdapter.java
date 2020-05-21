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
import ch.epfl.sdp.db.DatabaseObject;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * Custom RecyclerView adapter to display lists of events
 * @see RecyclerView.Adapter
 */
public class EventListAdapter extends RecyclerView.Adapter<EventListAdapter.EventViewHolder> {

    /**
     * Interface defining a listener for the adapter's item
     * on click events.
     */
    public interface OnItemClickListener {

        /**
         * Called when the user clicks on an event of the adapter.
         * @param event The event that was clicked on.
         */
        void OnItemClicked(@NonNull DatabaseObject<Event> event);
    }

    /**
     * ViewHolder that can display an event in an {@link EventListAdapter}
     */
    public static class EventViewHolder extends RecyclerView.ViewHolder {

        public TextView mTitle;
        public TextView mDate;

        public EventViewHolder(@NonNull View itemView) {
            super(itemView);
            mTitle = itemView.findViewById(R.id.eventlist_item_title);
            mDate = itemView.findViewById(R.id.eventlist_item_date);
        }

        /**
         * Set the click listener for this view holder.
         * @param event The event associated with this view holder.
         * @param listener The listener function.
         * @throws IllegalArgumentException One or more argument is null.
         */
        public void setClickListener(@NonNull DatabaseObject<Event> event, @NonNull OnItemClickListener listener) {
            verifyNotNull(event, listener);
            itemView.setOnClickListener(v -> listener.OnItemClicked(event));
        }
    }

    private List<DatabaseObject<Event>> mEventList;
    private OnItemClickListener mItemClickListener = null;

    /**
     * Create a new empty EventListAdapter
     */
    public EventListAdapter() {
        mEventList = new ArrayList<>();
    }

    /**
     * Create a new EventListAdapter containing the given list of events.
     * @param events The list of initial events.
     * @throws IllegalArgumentException The list of event is null.
     */
    public EventListAdapter(@NonNull List<DatabaseObject<Event>> events) {
        mEventList = verifyNotNull(events);
    }

    /**
     * Clear the adapter.
     * Automatically calls {@link RecyclerView.Adapter#notifyDataSetChanged()}
     */
    public void clear() {
        mEventList.clear();
        notifyDataSetChanged();
    }

    /**
     * Add a list of events to the adapter.
     * Automatically calls {@link RecyclerView.Adapter#notifyDataSetChanged()}
     * @param events The list of events to add to the adapter
     * @throws IllegalArgumentException The list of event is null.
     */
    public void addAll(@NonNull List<DatabaseObject<Event>> events) {
        mEventList.addAll(verifyNotNull(events));
        notifyDataSetChanged();
    }

    /**
     * Get an item at a certain position.
     * @param position The index of the object in the adapter.
     * @return The object at the specified index.
     */
    public DatabaseObject<Event> get(int position) {
        return mEventList.get(position);
    }

    /**
     * Set the click listener for the items of the adapter.
     * @param listener The on click listener for the adapter.
     * @throws IllegalArgumentException The listener is null.
     */
    public void setOnItemClickListener(@NonNull OnItemClickListener listener) {
        mItemClickListener = verifyNotNull(listener);
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
        DatabaseObject<Event> event = mEventList.get(position);
        holder.mTitle.setText(event.getObject().getTitle());
        holder.mDate.setText(event.getObject().getDateStr());
        if(mItemClickListener != null) {
            holder.setClickListener(event, mItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mEventList.size();
    }
}
