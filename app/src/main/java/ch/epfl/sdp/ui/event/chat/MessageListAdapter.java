package ch.epfl.sdp.ui.event.chat;

import android.content.Context;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.R;
import ch.epfl.sdp.User;
import ch.epfl.sdp.platforms.firebase.storage.ImageGetter;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;


/**
 * Adapter for the chat message list
 */
public class MessageListAdapter extends RecyclerView.Adapter {

    private static final int VIEW_TYPE_MESSAGE_SENT = 0;
    private static final int VIEW_TYPE_MESSAGE_RECEIVED = 1;
    private static Context mContext;
    private static List<Pair<ChatMessage, LiveData<User>>> mMessageList;
    private static Map<ChatMessage, LiveData<User>> mMessageMap;

    private String mUid;

    /**
     * Constructor of the message list adapter
     *
     * @param uid the id of the user
     */
    public MessageListAdapter(@NonNull String uid, @NonNull Context context) {
        mMessageList = new ArrayList<>();
        mMessageMap = new HashMap<>();
        mUid = uid;
        mContext = verifyNotNull(context);
    }

    /**
     * Method to set a list of chat messages
     *
     * @param messages the list of messages
     */
    public void setChatList(@NonNull List<Pair<ChatMessage, LiveData<User>>> messages) {
        mMessageList = messages;
        for (int i = 0; i < messages.size(); ++i) {
            mMessageMap.put(messages.get(i).first, messages.get(i).second);
        }
        notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return mMessageList.size();
    }

    // Determines the appropriate ViewType according to the sender of the message.
    @Override
    public int getItemViewType(int position) {
        ChatMessage message = mMessageList.get(position).first;

        if (message.getUid().equals(mUid)) {
            // If the current user is the sender of the message
            return VIEW_TYPE_MESSAGE_SENT;
        } else {
            // If some other user sent the message
            return VIEW_TYPE_MESSAGE_RECEIVED;
        }
    }

    // Inflates the appropriate layout according to the ViewType.
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view;

        if (viewType == VIEW_TYPE_MESSAGE_SENT) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_message_sent, parent, false);
            return new SentMessageHolder(view);
        } else {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.cardview_message_received, parent, false);
            return new ReceivedMessageHolder(view);
        }
    }

    // Passes the message object to a ViewHolder so that the contents can be bound to UI.
    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder holder, int position) {
        ChatMessage message = mMessageList.get(position).first;

        switch (holder.getItemViewType()) {
            case VIEW_TYPE_MESSAGE_SENT:
                ((SentMessageHolder) holder).bind(message);
                break;
            case VIEW_TYPE_MESSAGE_RECEIVED:
                ((ReceivedMessageHolder) holder).bind(message);
        }
    }

    private static class SentMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText;

        SentMessageHolder(View itemView) {
            super(itemView);
            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getText());
            timeText.setText(message.getDateStr());
        }
    }

    private static class ReceivedMessageHolder extends RecyclerView.ViewHolder {
        TextView messageText, timeText, nameText;
        ImageView profileImage;

        ReceivedMessageHolder(View itemView) {
            super(itemView);

            messageText = itemView.findViewById(R.id.text_message_body);
            timeText = itemView.findViewById(R.id.text_message_time);
            nameText = itemView.findViewById(R.id.text_message_name);
            profileImage = itemView.findViewById(R.id.image_message_profile);
        }

        void bind(ChatMessage message) {
            messageText.setText(message.getText());
            timeText.setText(message.getDateStr());
            nameText.setText(message.getName());

            if (mMessageMap.containsKey(message) && mMessageMap.get(message) != null)
                mMessageMap.get(message).observeForever(user -> {
                    ImageGetter.getInstance().getImage(mContext, user.getImageId(), profileImage);
                });
        }
    }

}
