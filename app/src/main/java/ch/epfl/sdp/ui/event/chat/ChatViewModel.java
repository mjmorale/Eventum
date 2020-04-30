package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;
import ch.epfl.sdp.auth.UserInfo;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ChatViewModel extends ViewModel {

    static class ChatViewModelFactory extends DatabaseViewModelFactory {
        ChatViewModelFactory() {
            super(String.class, Authenticator.class);
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }
    }

    interface OnMessageAddedCallback {
        void onFailure(Exception exception);
    }

    private CollectionQuery mMessageCollection;
    private FilterQuery mOrderedMessagesQuery;
    private LiveData<List<ChatMessage>> mMessageLiveData;
    private UserInfo mUser;

    public ChatViewModel(@NonNull String eventRef, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database, eventRef, authenticator);

        mMessageCollection = database.query("events").document(eventRef).collection("messages");
        mOrderedMessagesQuery = mMessageCollection.orderBy("date");
        mUser = authenticator.getCurrentUser();
    }

    public void addMessage(@NonNull String message, @NonNull OnMessageAddedCallback callback) {

        ChatMessage chatMessage = new ChatMessage(message, new Date(), mUser.getUid(), mUser.getDisplayName());
        mMessageCollection.create(chatMessage, res -> {
            if (!res.isSuccessful()) {
                callback.onFailure(res.getException());
            }
        });
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
