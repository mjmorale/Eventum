package ch.epfl.sdp.ui.event;

import java.io.File;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.Event;
import ch.epfl.sdp.db.Database;
import ch.epfl.sdp.db.queries.DocumentQuery;
import ch.epfl.sdp.ui.DatabaseViewModelFactory;

import static ch.epfl.sdp.ObjectUtils.verifyNotNull;

/**
 * View model to display some detail information of an event
 */
public class DefaultEventViewModel extends ViewModel {

    /**
     * Factory of the DefaultEventViewModel
     */
    static class DefaultEventViewModelFactory extends DatabaseViewModelFactory {

        /**
         * Constructor of the DefaultEventViewModel factory
         */
        DefaultEventViewModelFactory() {
            super(String.class);
        }

        /**
         * Method to set the reference of an event to the factory
         *
         * @param eventRef the reference of the event to display
         */
        void setEventRef(@NonNull String eventRef) {
            setValue(0, verifyNotNull(eventRef));
        }

    }

    private LiveData<Event> mEvent;

    private final DocumentQuery mEventDocumentQuery;
    private final String mEventRef;

    /**
     * Constructor of the DefaultEventViewModel, the factory should be used instead of this
     *
     * @param eventRef the reference of the event to display
     * @param database The database service to use
     */
    public DefaultEventViewModel(@NonNull String eventRef, @NonNull Database database) {
        verifyNotNull(eventRef, database);
        mEventRef = eventRef;
        mEventDocumentQuery = database.query("events").document(eventRef);
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
     * Method to get the reference of the specific event
     *
     * @return the reference of the event
     */
    public String getEventRef(){
        return mEventRef;
    }

}
