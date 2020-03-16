package ch.epfl.sdp;

import androidx.lifecycle.LiveData;

public class MockEventLiveData extends LiveData<Event> {
    @Override
    public void onActive() {
        setValue(MockEvents.getCurrentEvent());
    }
}
