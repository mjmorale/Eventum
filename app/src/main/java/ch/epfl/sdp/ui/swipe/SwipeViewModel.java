package ch.epfl.sdp.ui.swipe;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import ch.epfl.sdp.Event;

public class SwipeViewModel extends ViewModel {
    private MutableLiveData<Event> event;

    public MutableLiveData<Event> getEvent(){
        if(event == null){
            event= new MutableLiveData<>();
        }
        return event;
    }

}
