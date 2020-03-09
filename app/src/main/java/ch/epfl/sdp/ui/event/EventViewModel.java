package ch.epfl.sdp.ui.event;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;

public class EventViewModel extends ViewModel {
    public MutableLiveData<Event> getEvent() {
        if (event == null) {
            event = new MutableLiveData<>();
        }
        return event;
    }

    private MutableLiveData<Event> event;
}
