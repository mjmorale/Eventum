package ch.epfl.sdp.ui.event;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import ch.epfl.sdp.Event;

public class EventViewModel extends ViewModel {
    public EventViewModel(MutableLiveData<Event> event) {
        this.event = event;
    }

    public MutableLiveData<Event> getEvent() {
        return event;
    }

    private MutableLiveData<Event> event;
}
