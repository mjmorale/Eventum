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
            super(Database.class, String.class, FirebaseAuthenticator.class);
        }

        void setDatabase(@NonNull Database database) {
            setValue(0, verifyNotNull(database));
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(1, verifyNotNull(eventRef));
        }

        void setFirebaseAuthenticator(@NonNull FirebaseAuthenticator firebaseAuthenticator){setValue(2,verifyNotNull(firebaseAuthenticator));}
    }

    private final Database mDatabase;
    private final String mEventRef;

    private  CollectionQuery mMessageCollection;
    private  FilterQuery mOrderedMessagesQuery;
    private LiveData<List<ChatMessage>> mMessageLiveData;
    private UserInfo mUser;
    private FirebaseAuthenticator mFirebaseAuthenticator;

    public ChatViewModel(@NonNull Database database, @NonNull String eventRef, FirebaseAuthenticator firebaseAuthenticator) {
        verifyNotNull(database, eventRef, firebaseAuthenticator);
        mDatabase = database;
        mEventRef = eventRef;
        mFirebaseAuthenticator = firebaseAuthenticator;

    }

    public void addMessage(@NonNull String message) {
        ChatMessage chatMessage = new ChatMessage(message, new Date(), getUser().getUid(), getUser().getDisplayName());
        getMessageCollection().create(chatMessage, res -> { });
    }

    public String getUserRef() {
        return getUser().getUid();
    }

    public LiveData<List<ChatMessage>> getMessages() {
        if (mMessageLiveData == null)
           mMessageLiveData = getOrderedMessagesQuery().livedata(ChatMessage.class);

        return mMessageLiveData;
    }

    private CollectionQuery getMessageCollection(){
        if(mMessageCollection==null)
            mMessageCollection = mDatabase.query("events").document(mEventRef).collection("messages");
        return  mMessageCollection;
    }

    private FilterQuery getOrderedMessagesQuery() {
        if(mOrderedMessagesQuery==null)
            mOrderedMessagesQuery = getMessageCollection().orderBy("date");
        return mOrderedMessagesQuery;
    }

    private UserInfo getUser(){
        if(mUser ==null)
            mUser = mFirebaseAuthenticator.getCurrentUser();
        return  mUser;
    }
}
