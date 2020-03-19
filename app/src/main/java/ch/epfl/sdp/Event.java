package ch.epfl.sdp;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.Date;

public class Event {
    final private String mDescription;
    final private Date mDate;
    final private String mTitle;
    final private int mImageID;
    final private LatLng mLocation;

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date) {
        this(title, description, date, R.mipmap.ic_launcher);
    }

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date, @NonNull int imageID){
        this(title,description,date,imageID,new LatLng(46.520553, 6.567821));
    }

    public Event(@NonNull String title, @NonNull String description, @NonNull Date date, @NonNull int imageID, @NonNull LatLng location){
        this.mTitle = title;
        this.mDescription = description;
        this.mDate = date;
        this.mImageID = imageID;
        this.mLocation = location;
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

    public LatLng getLocation() {
        return mLocation;
    }

    public int getImageID() {
        return mImageID;
    }
}
