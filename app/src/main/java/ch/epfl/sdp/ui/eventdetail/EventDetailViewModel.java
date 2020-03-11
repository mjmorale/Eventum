package ch.epfl.sdp.ui.eventdetail;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.Event;

public class EventDetailViewModel extends ViewModel {
    private MutableLiveData<Event> mdata = new MutableLiveData<Event>();

    public MutableLiveData<Event> getEvent(){
        return  mdata;
    }

}
