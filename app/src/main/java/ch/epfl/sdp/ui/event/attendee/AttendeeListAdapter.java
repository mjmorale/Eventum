package ch.epfl.sdp.ui.event.attendee;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.DatabaseObject;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendeeListAdapter extends RecyclerView.Adapter<AttendeeListAdapter.UserViewHolder> {

    public interface OnItemClickListener {

        void OnItemClicked(@NonNull DatabaseObject<User> event);
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public TextView mName;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            mName = itemView.findViewById(R.id.attendee_list_name);
        }

        public void setClickListener(@NonNull DatabaseObject<User> user, @NonNull AttendeeListAdapter.OnItemClickListener listener) {
            verifyNotNull(user, listener);
            itemView.setOnClickListener(v -> listener.OnItemClicked(user));
        }
    }

    private List<DatabaseObject<User>> mUserList;
    private AttendeeListAdapter.OnItemClickListener mItemClickListener = null;
    private String mCurrentUser;

    public AttendeeListAdapter(@NonNull String currentUser) {
        mCurrentUser = verifyNotNull(currentUser);
        mUserList = new ArrayList<>();
    }

    public AttendeeListAdapter(@NonNull String currentUser, @NonNull List<DatabaseObject<User>> events) {
        mCurrentUser = verifyNotNull(currentUser);
        mUserList = verifyNotNull(events);
    }

    public void clear() {
        mUserList.clear();
        notifyDataSetChanged();
    }

    public void addAll(@NonNull List<DatabaseObject<User>> users) {
        mUserList.addAll(verifyNotNull(users));
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(@NonNull AttendeeListAdapter.OnItemClickListener listener) {
        mItemClickListener = verifyNotNull(listener);
    }

    @NonNull
    @Override
    public AttendeeListAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.cardview_attendeelist_item, parent, false);
        AttendeeListAdapter.UserViewHolder viewHolder = new AttendeeListAdapter.UserViewHolder(v);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull AttendeeListAdapter.UserViewHolder holder, int position) {
        DatabaseObject<User> user = mUserList.get(position);
        holder.mName.setText(user.getObject().getName());
        if(mItemClickListener != null) {
            holder.setClickListener(user, mItemClickListener);
        }
    }

    @Override
    public int getItemCount() {
        return mUserList.size();
    }
}
