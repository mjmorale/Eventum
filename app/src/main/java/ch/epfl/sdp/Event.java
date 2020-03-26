package ch.epfl.sdp;

import android.location.Geocoder;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Event {
    private String mDescription;
    private Date mDate;
    private String mTitle;
    private int mImageID;
    private String mAddress;
    private LatLng mLocation;
    private SimpleDateFormat mFormatter = new SimpleDateFormat("dd/MM/yyyy");

    public Event(@NonNull String title,
                 @NonNull String description,
                 @NonNull Date date,
                 @NonNull String address,
                 @NonNull LatLng location,
                 @NonNull int imageID) {
        mTitle = title;
        mDescription = description;
        mDate = date;
        mAddress = address;
        mLocation = location;
        mImageID = imageID;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getDescription() {
        return mDescription;
    }

    public Date getDate() {
        return mDate;
    }

    public String getDateStr() {
        return mFormatter.format(mDate);
    }

    public LatLng getLocation() {
        return mLocation;
    }

    public int getImageID() {
        return mImageID;
    }

    public void setTitle(String title) {
        if (title == null)
            throw new IllegalArgumentException();
        this.mTitle = title;
    }

    public void setDescription(String description) {
        if (description == null)
            throw new IllegalArgumentException();
        this.mDescription = description;
    }

    public void setDate(Date date) {
        if (date == null)
            throw new IllegalArgumentException();
        this.mDate = date;
    }

    public void setDate(String date) {
        try {
            this.mDate = mFormatter.parse(date);
        } catch (Exception e) {
            throw new IllegalArgumentException();
        }
    }

    public void setLocation(LatLng location) {
        if (location == null)
            throw new IllegalArgumentException();
        this.mLocation = location;
    }

    public void setImageID(@NonNull int imageName) {
        this.mImageID = imageName;
    }

    public String getAddress() { return mAddress; }

    public void setAddress(String mAddress) { this.mAddress = mAddress; }

    private SimpleDateFormat getFormatter() { return mFormatter; }
}
