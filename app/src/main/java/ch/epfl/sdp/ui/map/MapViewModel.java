package ch.epfl.sdp.ui.map;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.Set;

import ch.epfl.sdp.Event;

public class MapViewModel extends ViewModel {
    private MutableLiveData<Set<Event>> mEvents;

    public MutableLiveData<Set<Event>> getEvents() {
        if (mEvents == null) {
            mEvents = new MutableLiveData<Set<Event>>();
            loadEvents();
        }
        return mEvents;
    }

    private void loadEvents() {

    }
}