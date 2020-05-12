package ch.epfl.sdp.ui.event.attendee;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

public class AttendeeViewModel extends ViewModel {

    static class AttendeeViewModelFactory extends DatabaseViewModelFactory {
        AttendeeViewModelFactory() {
            super(String.class, Authenticator.class);
        }

        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

        void setAuthenticator(@NonNull Authenticator authenticator) {
            setValue(1, verifyNotNull(authenticator));
        }
    }

    private final String mUserRef;
    private MutableLiveData<List<DatabaseObject<User>>> mUsers = new MutableLiveData<>();

    public AttendeeViewModel(@NonNull String eventRef, @NonNull Authenticator authenticator, @NonNull Database database) {
        mUserRef = authenticator.getCurrentUser().getUid();
        database.query("events").document(eventRef).getField("attendees", eventRes -> {
            if(eventRes.isSuccessful()) {
                List<String> attendeeIds = (ArrayList<String>)eventRes.getData();
                database.query("users").get(User.class, usersRes -> {
                    if(usersRes.isSuccessful()) {
                        List<DatabaseObject<User>> attendee = new ArrayList<>();
                        for(DatabaseObject<User> user: usersRes.getData()) {
                            if(attendeeIds.contains(user.getId())) {
                                attendee.add(user);
                            }
                        }
                        mUsers.postValue(attendee);
                    }
                });
            }
        });
    }

    public LiveData<List<DatabaseObject<User>>> getAttendee() {
        return mUsers;
    }

    public String getUserRef() {
        return mUserRef;
    }
}
