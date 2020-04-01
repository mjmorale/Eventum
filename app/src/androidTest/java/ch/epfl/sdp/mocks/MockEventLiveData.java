package ch.epfl.sdp.mocks;

import androidx.lifecycle.LiveData;
import ch.epfl.sdp.Event;

public class MockEventLiveData extends LiveData<Event> {
    @Override
    public void onActive() {
        Event event = MockEvents.getCurrentEvent();

        setValue(MockEvents.getCurrentEvent());
    }
}
