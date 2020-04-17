package ch.epfl.sdp.ui.event.chat;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.ui.ParameterizedViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class ChatViewModel extends ViewModel {

    static class ChatViewModelFactory extends ParameterizedViewModelFactory {

        // The constructor describes the types of the parameters that will eventually be
        // passed to the view model's constructor, in our case two strings.
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

    // TODO: Implement the ViewModel
}
