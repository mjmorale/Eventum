package ch.epfl.sdp.ui.event;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import ch.epfl.sdp.Event;

public class EventViewModel extends ViewModel {
    public MutableLiveData<Event> getEvent() {
        if (event == null) {
            event = new MutableLiveData<>();
        }
        return event;
    }

    public String formatDate(Date date) {
        return formatter.format(date);
    }

    public void createEvent(String title, String description, String date) {
        try {
            Date formatDate = formatter.parse(date);

        } catch (ParseException e) {
            e.printStackTrace();
        }

    }

    private MutableLiveData<Event> event;
    private SimpleDateFormat formatter = new SimpleDateFormat("dd-MMM-yyyy");
}
