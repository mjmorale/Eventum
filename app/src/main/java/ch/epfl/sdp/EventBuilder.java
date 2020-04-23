package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.util.Date;

public class EventBuilder {
    private String mTitle;
    private String mDescription;
    private Date mDate;
    private String mAddress = "Lausanne, Switzerland";
    private LatLng mLocation = new LatLng(10, 10);
    private String mImageId;
    private final String DEFAULT_URL = "https://firebasestorage.googleapis.com/v0/b/eventum-6a6b7.appspot.com" +
            "/o/eventDefault.jpg?alt=media&token=a6d345fa-a513-478d-a019-2307ee50022b";

    public Event build() {
        if (mImageId == null) mImageId = DEFAULT_URL;
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

    public EventBuilder setDate(String date) {
        try {
            this.mDate = Event.parseDate(date);
        } catch (ParseException e) {
            this.mDate = new Date();
        }
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

    public EventBuilder setImageId(String imageId) {
        this.mImageId = imageId;
        return this;
    }
}
