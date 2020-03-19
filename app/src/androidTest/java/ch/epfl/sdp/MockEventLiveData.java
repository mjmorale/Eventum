package ch.epfl.sdp;

import androidx.lifecycle.LiveData;

public class MockEventLiveData extends LiveData<Event> {
    @Override
    public void onActive() {
        Event event = MockEvents.getCurrentEvent();

        setValue(MockEvents.getCurrentEvent());
    }
}
