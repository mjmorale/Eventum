package ch.epfl.sdp;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class EventBuilder {
    private String mTitle;
    private String mDescription;
    private Date mDate;
    private String mAddress;
    private LatLng mLocation;
    private int mImageId = R.mipmap.ic_launcher;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public Event build() {
        if (mTitle == "") throw new IllegalStateException("No title specified");
        if (mDescription == "") throw new IllegalStateException("No description specified");
        if (mDate == null) throw new IllegalStateException("No date specified");
        if (mAddress == "") throw new IllegalStateException("No address specified");
        if (mLocation == null) throw new IllegalStateException("No location specified");
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
        this.mDate = mFormatter.parse(date);
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
