package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.google.firebase.auth.FirebaseAuth;


import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.platforms.firebase.auth.FirebaseAuthenticator;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import ch.epfl.sdp.auth.UserInfo;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ChatViewModel extends ViewModel {

    static class ChatViewModelFactory extends ParameterizedViewModelFactory {

        ChatViewModelFactory() {
            super(Database.class, String.class, Authenticator.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(1, verifyNotNull(eventRef));
        }

        void setAuthenticator(@NonNull Authenticator authenticator){
            setValue(2, verifyNotNull(authenticator));
        }
    }

    interface OnMessageAddedCallback {
        void onSuccess(String messageRef);
        void onFailure(Exception exception);
    }

    private CollectionQuery mMessageCollection;
    private FilterQuery mOrderedMessagesQuery;
    private LiveData<List<ChatMessage>> mMessageLiveData;
    private UserInfo mUser;

    public ChatViewModel(@NonNull Database database, @NonNull String eventRef, Authenticator authenticator) {
        verifyNotNull(database, eventRef, authenticator);

        mMessageCollection = database.query("events").document(eventRef).collection("messages");
        mOrderedMessagesQuery = mMessageCollection.orderBy("date");
        mUser = authenticator.getCurrentUser();

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
