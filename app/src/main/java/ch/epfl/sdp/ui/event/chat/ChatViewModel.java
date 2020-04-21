package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;


import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import ch.epfl.sdp.auth.UserInfo;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ChatViewModel extends ViewModel {

    static class ChatViewModelFactory extends ParameterizedViewModelFactory {

        ChatViewModelFactory() {
            super(Database.class, String.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(1, verifyNotNull(eventRef));
        }
    }

    interface OnMessageAddedCallback {
        void onSuccess(String messageRef);
        void onFailure(Exception exception);
    }

    private final CollectionQuery mMessageCollection;
    private final FilterQuery mOrderedMessagesQuery;
    private final Database mDatabase;
    private final String mEventRef;
    private LiveData<List<ChatMessage>> mMessageLiveData;
    private UserInfo mUser;

    public ChatViewModel(@NonNull Database database, @NonNull String eventRef) {
        verifyNotNull(database, eventRef);
        mDatabase = database;
        mEventRef = eventRef;
        mMessageCollection = mDatabase.query("events").document(mEventRef).collection("messages");
        mOrderedMessagesQuery = mMessageCollection.orderBy("date");

        mUser = new FirebaseAuthenticator(FirebaseAuth.getInstance()).getCurrentUser();
    }

    public void addMessage(@NonNull String message, @NonNull OnMessageAddedCallback callback) {

        ChatMessage chatMessage = new ChatMessage(message, new Date(), mUser.getUid(), mUser.getDisplayName());
        mMessageCollection.create(chatMessage, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(res.getData());
            } else {
                callback.onFailure(res.getException());
            }
        });
    }

    public String getEventRef(){
        return mEventRef;
    }

    public String getUserRef() {
        return mUser.getUid();
    }

    public LiveData<List<ChatMessage>> getMessages() {
        if (mMessageLiveData == null) {
           mMessageLiveData = mOrderedMessagesQuery.livedata(ChatMessage.class);
        }
        return mMessageLiveData;

    }

}
