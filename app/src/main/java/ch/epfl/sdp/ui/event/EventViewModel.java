package ch.epfl.sdp.ui.event;

import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MediatorLiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.User;
import ch.epfl.sdp.auth.Authenticator;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.DatabaseObject;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model to display some detail information of an event
 */
public class EventViewModel extends ViewModel {

    /**
     * Factory of the DefaultEventViewModel
     */
    static class DefaultEventViewModelFactory extends DatabaseViewModelFactory {

        /**
         * Constructor of the DefaultEventViewModel factory
         */
        DefaultEventViewModelFactory() {
            super(String.class, Authenticator.class);
        }

        /**
         * Method to set the reference of an event to the factory
         *
         * @param eventRef the reference of the event to display
         */
        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

        /**
         * Set the instance of the authenticator service.
         *
         * @param authenticator The authenticator instance to use
         */
        void setAuthenticator(@NonNull Authenticator authenticator) { setValue(1, verifyNotNull(authenticator)); }

    }

    private LiveData<Event> mEvent;

    private final DocumentQuery mEventDocumentQuery;
    private final String mEventRef;

    private final String mUserRef;
    private MutableLiveData<List<DatabaseObject<User>>> mAttendees = new MutableLiveData<>();
    private MediatorLiveData<User> mOrganizer = new MediatorLiveData<>();

    /**
     * Constructor of the DefaultEventViewModel, the factory should be used instead of this
     *
     * @param eventRef the reference of the event to display
     * @param database The database service to use
     */
    public EventViewModel(@NonNull String eventRef, @NonNull Authenticator authenticator, @NonNull Database database) {
        verifyNotNull(eventRef, database);
        mEventRef = eventRef;
        mUserRef = authenticator.getCurrentUser().getUid();
        mEventDocumentQuery = database.query("events").document(eventRef);

        mEventDocumentQuery.getField("organizer", organizerRes -> {
            if(organizerRes.isSuccessful()) {
                String organizerRef = (String) organizerRes.getData();
                mOrganizer.addSource(database.query("users").document(organizerRef).liveData(User.class), user -> {
                    mOrganizer.postValue(user);
                });
            }
        });
        mEventDocumentQuery.getField("attendees", eventRes -> {
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
                        mAttendees.postValue(attendee);
                    }
                });
            }
        });
    }

    /**
     * Method to get the specific event in the database
     *
     * @return a live data of this event
     */
    public LiveData<Event> getEvent() {
        if(mEvent == null) {
            mEvent = mEventDocumentQuery.liveData(Event.class);
        }
        return mEvent;
    }

    /**
     * Method to get the event organizer
     *
     * @return a live data of the event's organizer
     */
    public LiveData<User> getOrganizer() {
        return mOrganizer;
    }

    /**
     * @return A liveData on the list of attendees to the current event
     */
    public LiveData<List<DatabaseObject<User>>> getAttendees() {
        return mAttendees;
    }

    /**
     * Method to get the reference of the specific event
     *
     * @return the reference of the event
     */
    public String getEventRef(){
        return mEventRef;
    }

    /**
     * @return The current user's reference
     */
    public String getUserRef() {
        return mUserRef;
    }

}
