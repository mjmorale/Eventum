package ch.epfl.sdp.ui.main.attending;

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

public class AttendingEventAdapter extends RecyclerView.Adapter<AttendingEventAdapter.AttendingEventViewHolder> {

    interface OnItemClickListener {
        void onItemClicked(DatabaseObject<Event> event);
    }

    private List<DatabaseObject<Event>> mAttendingEvents;
    private OnItemClickListener mItemClickListener;

    public static class AttendingEventViewHolder extends RecyclerView.ViewHolder {

        private View mItemView;
        public TextView mTitle;

        public AttendingEventViewHolder(@NonNull View itemView) {
            super(itemView);
            mItemView = itemView;
            mTitle = itemView.findViewById(R.id.attending_event_title);
        }

        public void bindClickListener(@NonNull DatabaseObject<Event> event, @NonNull OnItemClickListener listener) {
            verifyNotNull(event, listener);
            mItemView.setOnClickListener(v -> {
                listener.onItemClicked(event);
            });
        }
    }

    public AttendingEventAdapter() {
        this(new ArrayList<>());
    }

    public AttendingEventAdapter(@NonNull List<DatabaseObject<Event>> attendingEvents) {
        mAttendingEvents = verifyNotNull(attendingEvents);
        mItemClickListener = null;
    }

    public void setAttendingEvents(@NonNull List<DatabaseObject<Event>> attendingEvents) {
        mAttendingEvents = verifyNotNull(attendingEvents);
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        mItemClickListener = listener;
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
        DatabaseObject<Event> event = mAttendingEvents.get(position);
        holder.mTitle.setText(event.getObject().getTitle());
        if(mItemClickListener != null) {
            holder.bindClickListener(event, mItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mAttendingEvents.size();
    }
}
