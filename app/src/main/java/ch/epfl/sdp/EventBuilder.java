package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventBuilder {
    private String mTitle;
    private String mDescription;
    private Date mDate;
    private String mAddress = "Lausanne, Switzerland";
    private LatLng mLocation = new LatLng(10, 10);
    private int mImageId = R.mipmap.ic_launcher;

    public Event build() {
        ObjectUtils.verifyNotNull(mTitle, mDescription, mDate, mLocation, mImageId);
        if (mTitle.isEmpty()) throw new IllegalArgumentException("No title specified");
        if (mDescription.isEmpty()) throw new IllegalArgumentException("No description specified");
        return new Event(mTitle, mDescription, mDate, mAddress, mLocation, mImageId);
    }

    public EventBuilder setTitle(String title) {
        this.mTitle = title;
        return this;
    }

    public EventBuilder setDescription(String description) {
        this.mDescription = description;
        return this;
    }

    public EventBuilder setDate(Date date) {
        this.mDate = date;
        return this;
    }

    public EventBuilder setDate(String date) throws ParseException {
        this.mDate = Event.parseDate(date);
        return this;
    }

    public EventBuilder setAddress(String address) {
        this.mAddress = address;
        return this;
    }

    public EventBuilder setLocation(LatLng location) {
        this.mLocation = location;
        return this;
    }

    public EventBuilder setImageId(int imageId) {
        this.mImageId = imageId;
        return this;
    }
}
