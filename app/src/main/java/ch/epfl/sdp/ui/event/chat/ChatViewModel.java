package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.Date;
import java.util.List;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.auth.UserInfo;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.db.queries.FilterQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model for the chat (to chat between users about an event)
 */
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
    private LiveData<List<DatabaseObject<ChatMessage>>> mMessageLiveData;
    private UserInfo mUser;
    private Database mDatabase;

    /**
     * Constructor of the chat view model
     *
     * @param database      where the chat messages will be uploaded
     * @param eventRef      the reference of an event
     * @param authenticator of the user
     */
    public ChatViewModel(@NonNull String eventRef, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(database, eventRef, authenticator);
        mDatabase = database;
        mMessageCollection = database.query("events").document(eventRef).collection("messages");
        mOrderedMessagesQuery = mMessageCollection.orderBy("date");
        mUser = authenticator.getCurrentUser();
    }

    public LiveData<User> getUser(String userId){
       return mDatabase.query("users").document(userId).liveData(User.class);
    }


    /**
     * Method to add a new chat message for an event in the database
     *
     * @param message  to be uploaded in the database
     * @param callback called when the upload is done
     */
    public void addMessage(@NonNull String message, @NonNull OnMessageAddedCallback callback) {
        ChatMessage chatMessage = new ChatMessage(message, new Date(), mUser.getUid(), mUser.getDisplayName());
        mMessageCollection.create(chatMessage, res -> {
            if (!res.isSuccessful()) {
                callback.onFailure(res.getException());
            }
        });
    }

    /**
     * Method to get the reference of the user
     *
     * @return the reference of the user
     */
    public String getUserRef() {
        return mUser.getUid();
    }

    /**
     * Method to get the chat messages
     *
     * @return the messages
     */
    public LiveData<List<DatabaseObject<ChatMessage>>> getMessages() {
        if (mMessageLiveData == null) {
            mMessageLiveData = mOrderedMessagesQuery.liveData(ChatMessage.class);
        }
        return mMessageLiveData;
    }



}
