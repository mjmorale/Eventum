package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import java.util.Date;

import ch.epfl.sdp.ChatMessage;
import ch.epfl.sdp.User;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.CollectionQuery;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;
import ch.epfl.sdp.ui.auth.AuthViewModel;


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

    private final CollectionQuery mEventCollection;
    private final Database mDatabase;
    private final String mEventRef;
    private User mUser;

    public ChatViewModel(@NonNull Database database, @NonNull String eventRef) {
        verifyNotNull(database, eventRef);
        mDatabase = database;
        mEventRef = eventRef;
        mEventCollection = database.query("events").document(eventRef).collection("messages");

        //mUser = new ViewModelProvider(this, new AuthViewModel.AuthViewModelFactory()).get(AuthViewModel.class).getUser();

    }

    public void addMessage(@NonNull String message, @NonNull OnMessageAddedCallback callback) {

        ChatMessage chatMessage = new ChatMessage(message, new Date(), "a", "b");
        mEventCollection.create(chatMessage, res -> {
            if(res.isSuccessful()) {
                callback.onSuccess(res.getData());
            } else {
                callback.onFailure(res.getException());
            }
        });
    }
}
