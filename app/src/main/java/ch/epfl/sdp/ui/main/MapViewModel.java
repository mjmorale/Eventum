package ch.epfl.sdp.ui.main;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Set;

import ch.epfl.sdp.Event;

public class MapViewModel extends ViewModel {
    private MutableLiveData<Set<Event>> events;

    public MutableLiveData<Set<Event>> getEvents() {
        if (events == null) {
            events = new MutableLiveData<Set<Event>>();
            loadEvents();
        }
        return events;
    }

    private void loadEvents() {

    }


}